/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guardian.go.articles.ui.fragments;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * A {@link RecyclerView.LayoutManager} which displays a regular grid (i.e. all cells are the same
 * size) and allows simultaneous row & column spanning.
 */
public class SpannedGridLayoutManager extends LinearLayoutManager {

    private GridSpanLookup spanLookup;
    private int columns = 1;
    private float cellAspectRatio = 1f;
    private int additionalCellHeight = 0;

    private int cellHeight;
    private int[] cellBorders;
    private int firstVisiblePosition;
    private int lastVisiblePosition;
    private int firstVisibleRow;
    private int lastVisibleRow;
    private boolean forceClearOffsets;
    private SparseArray<GridCell> cells;
    private List<Integer> firstChildPositionForRow; // key == row, val == first child position
    private int totalRows;
    private final Rect itemDecorationInsets = new Rect();

    public SpannedGridLayoutManager(Context context, GridSpanLookup spanLookup, int columns, float cellAspectRatio, int additionalCellHeight) {
        super(context);
        this.spanLookup = spanLookup;
        this.columns = columns;
        this.cellAspectRatio = cellAspectRatio;
        this.additionalCellHeight = additionalCellHeight;
        setAutoMeasureEnabled(true);
    }

    /*
    @Keep /* XML constructor, see RecyclerView#createLayoutManager */
    /*
    public SpannedGridLayoutManager(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.SpannedGridLayoutManager, defStyleAttr, defStyleRes);
        columns = a.getInt(R.styleable.SpannedGridLayoutManager_spanCount, 1);
        parseAspectRatio(a.getString(R.styleable.SpannedGridLayoutManager_aspectRatio));
        // TODO use this!
        int orientation = a.getInt(
                R.styleable.SpannedGridLayoutManager_android_orientation, RecyclerView.VERTICAL);
        a.recycle();
        setAutoMeasureEnabled(true);
    }
    */

    public interface GridSpanLookup {
        SpanInfo getSpanInfo(int position);
    }

    public void setSpanLookup(@NonNull GridSpanLookup spanLookup) {
        this.spanLookup = spanLookup;
    }

    public static class SpanInfo {
        public int columnSpan;
        public int rowSpan;

        public SpanInfo(int columnSpan, int rowSpan) {
            this.columnSpan = columnSpan;
            this.rowSpan = rowSpan;
        }

        public static final SpanInfo SINGLE_CELL = new SpanInfo(1, 1);
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {

        int columnSpan;
        int rowSpan;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        calculateWindowSize();
        calculateCellPositions(recycler, state);

        if (state.getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            firstVisibleRow = 0;
            resetVisibleItemTracking();
            return;
        }

        // TODO use orientationHelper
        int startTop = getPaddingTop();
        int scrollOffset = 0;
        if (forceClearOffsets) { // see #scrollToPosition
            startTop = 0;//-(firstVisibleRow * cellHeight);
            forceClearOffsets = false;
        } else if (getChildCount() != 0) {
            scrollOffset = getDecoratedTop(getChildAt(0));
            startTop = scrollOffset;// - (firstVisibleRow * cellHeight);
            resetVisibleItemTracking();
        }

        detachAndScrapAttachedViews(recycler);
        int row = firstVisibleRow;
        int availableSpace = getHeight() - scrollOffset;
        int lastItemPosition = state.getItemCount() - 1;
        if (lastVisiblePosition == 0 && lastItemPosition == 0) {
            startTop += layoutRowGroup(row, startTop, true, recycler, state);
            row = getNextSpannedRow(row);
        }
        while (startTop < availableSpace && lastVisiblePosition < lastItemPosition) {
            startTop += layoutRowGroup(row, startTop, true, recycler, state);
            row = getNextSpannedRow(row);
        }

        layoutDisappearingViews(recycler, state, startTop);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        } else {
            return new LayoutParams(lp);
        }
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        removeAllViews();
        reset();
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || dy == 0) return 0;

        int scrolled;
        int top = getDecoratedTop(getChildAt(0));

        if (dy < 0) { // scrolling content down
            if (firstVisibleRow == 0) { // at top of content
                int scrollRange = -(getPaddingTop() - top);
                scrolled = Math.max(dy, scrollRange);
            } else {
                scrolled = dy;
            }
            if (top - scrolled >= 0) { // new top row came on screen
                int newRow = getPreviousSpannedRow(firstVisibleRow);
                if (newRow >= 0) {
                    layoutRowGroup(newRow, top, false, recycler, state);
                }
            }
            int firstPositionOfLastRow = getFirstPositionInRowGroup(lastVisibleRow);
            int lastRowTop = getDecoratedTop(
                    getChildAt(firstPositionOfLastRow - firstVisiblePosition));
            if (lastRowTop - scrolled > getHeight()) { // last spanned row scrolled out
                recycleRow(lastVisibleRow, recycler, state);
            }
        } else { // scrolling content up
            int bottom = getDecoratedBottom(getChildAt(getChildCount() - 1));
            if (lastVisiblePosition == getItemCount() - 1) { // is at end of content
                int scrollRange = Math.max(bottom - getHeight() + getPaddingBottom(), 0);
                scrolled = Math.min(dy, scrollRange);
            } else {
                scrolled = dy;
            }
            if ((bottom - scrolled) < getHeight() && lastVisibleRow != 0) { // new row scrolled in
                int nextRow = getNextSpannedRow(lastVisibleRow);
                if (nextRow < getSpannedRowCount()) {
                    layoutRowGroup(nextRow, bottom, true, recycler, state);
                }
            }
            int lastPositionInRow = getLastPositionInRowGroup(firstVisibleRow, state);
            int bottomOfFirstRow =
                    getDecoratedBottom(getChildAt(lastPositionInRow - firstVisiblePosition));
            if (bottomOfFirstRow - scrolled < 0) { // first spanned row scrolled out
                recycleRow(firstVisibleRow, recycler, state);
            }
        }
        offsetChildrenVertical(-scrolled);
        return scrolled;
    }

    @Override
    public void scrollToPosition(int position) {
        if (position >= getItemCount()) position = getItemCount() - 1;

        firstVisibleRow = getRowTopIndex(position);
        resetVisibleItemTracking();
        forceClearOffsets = true;
        removeAllViews();
        requestLayout();
    }

    @Override
    public void smoothScrollToPosition(
            RecyclerView recyclerView, RecyclerView.State state, int position) {
        if (position >= getItemCount()) position = getItemCount() - 1;

        LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                final int rowOffset = getRowTopIndex(targetPosition) - firstVisibleRow;
                return new PointF(0, rowOffset * cellHeight);
            }
        };
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    @Override
    public int computeVerticalScrollRange(RecyclerView.State state) {
        // TODO update this to incrementally calculate
        if (firstChildPositionForRow == null) return 0;
        return getSpannedRowCount() * cellHeight + getPaddingTop() + getPaddingBottom();
    }

    @Override
    public int computeVerticalScrollExtent(RecyclerView.State state) {
        return getHeight();
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        if (getChildCount() == 0) return 0;
        return getPaddingTop() + (firstVisibleRow * cellHeight) - getDecoratedTop(getChildAt(0));
    }

    @Override
    public View findViewByPosition(int position) {
        if (position < firstVisiblePosition || position > lastVisiblePosition) return null;
        return getChildAt(position - firstVisiblePosition);
    }

    public int getFirstVisibleItemPosition() {
        return firstVisiblePosition;
    }

    private static class GridCell {
        final int row;
        final int rowSpan;
        final int column;
        final int columnSpan;

        GridCell(int row, int rowSpan, int column, int columnSpan) {
            this.row = row;
            this.rowSpan = rowSpan;
            this.column = column;
            this.columnSpan = columnSpan;
        }
    }

    /**
     * This is the main layout algorithm, iterates over all items and places them into [column, row]
     * cell positions. Stores this layout info for use later on. Also records the adapter position
     * that each row starts at.
     * <p>
     * Note that if a row is spanned, then the row start position is recorded as the first cell of
     * the row that the spanned cell starts in. This is to ensure that we have sufficient contiguous
     * views to layout/draw a spanned row.
     */
    private void calculateCellPositions(RecyclerView.Recycler recycler, RecyclerView.State state) {
        final int itemCount = state.getItemCount();

        // Initialise some state that this function is going to calculate
        cells = new SparseArray<>(itemCount);
        firstChildPositionForRow = new ArrayList<>();
        int row = 0;
        int column = 0;
        recordSpannedRowStartPosition(row, column);

        // This "High Water Mark" array records, for each column, the lowest row that is occupied
        // so far (it should be called "Low Water Mark" really).
        int[] rowHWM = new int[columns];

        // Main loop
        for (int position = 0; position < itemCount; position++) {

            SpanInfo spanInfo;
            int adapterPosition = recycler.convertPreLayoutPositionToPostLayout(position);
            if (adapterPosition != RecyclerView.NO_POSITION) {
                try {
                    spanInfo = spanLookup.getSpanInfo(adapterPosition);
                } catch (Exception e) {
                    // use fallback method to get span info
                    spanInfo = getSpanInfoFromAttachedView(position);
                }
            } else {
                // item removed from adapter, retrieve its previous span info
                // as we can't get from the lookup (adapter)
                spanInfo = getSpanInfoFromAttachedView(position);
            }

            if (spanInfo.columnSpan > columns) {
                spanInfo.columnSpan = columns; // or should we throw?
            }

            // check horizontal space at current position else start a new row
            // note that this may leave gaps in the grid; we don't backtrack to try and fit
            // subsequent cells into gaps. We place the responsibility on the adapter to provide
            // continuous data i.e. that would not span column boundaries to avoid gaps.
            if (column + spanInfo.columnSpan > columns) {
                row++;
                recordSpannedRowStartPosition(row, position);
                column = 0;
            }

            // check if this cell is already filled (by previous spanning cell)
            while (rowHWM[column] > row) {
                column++;
                if (column + spanInfo.columnSpan > columns) {
                    row++;
                    recordSpannedRowStartPosition(row, position);
                    column = 0;
                }
            }

            // by this point, cell should fit at [column, row]
            cells.put(position, new GridCell(row, spanInfo.rowSpan, column, spanInfo.columnSpan));

            // update the high water mark book-keeping
            for (int columnsSpanned = 0; columnsSpanned < spanInfo.columnSpan; columnsSpanned++) {
                rowHWM[column + columnsSpanned] = row + spanInfo.rowSpan;
            }

            // if we're spanning rows then record the 'first child position' as the first item
            // *in the row the spanned item starts*. i.e. the position might not actually sit
            // within the row but it is the earliest position we need to render in order to fill
            // the requested row.
            if (spanInfo.rowSpan > 1) {
                int rowStartPosition = getFirstPositionInRowGroup(row);
                for (int rowsSpanned = 1; rowsSpanned < spanInfo.rowSpan; rowsSpanned++) {
                    int spannedRow = row + rowsSpanned;
                    recordSpannedRowStartPosition(spannedRow, rowStartPosition);
                }
            }

            // increment the current position
            column += spanInfo.columnSpan;
        }
        totalRows = rowHWM[0];
        for (int i = 1; i < rowHWM.length; i++) {
            if (rowHWM[i] > totalRows) {
                totalRows = rowHWM[i];
            }
        }
    }

    private SpanInfo getSpanInfoFromAttachedView(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (position == getPosition(child)) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                return new SpanInfo(lp.columnSpan, lp.rowSpan);
            }
        }
        // errrrr?
        return SpanInfo.SINGLE_CELL;
    }

    private void recordSpannedRowStartPosition(final int rowIndex, final int position) {
        if (getSpannedRowCount() < (rowIndex + 1)) {
            firstChildPositionForRow.add(position);
        }
    }

    private int getRowTopIndex(final int position) {
        if (position < cells.size()) {
            GridCell gridCell = cells.get(position);
            return gridCell.row;
        } else {
            return -1;
        }
    }

    /**
     * Takes rowSpan in consideration
     */
    private int getRowBottomIndex(final int position) {
        if (position < cells.size()) {
            GridCell gridCell = cells.get(position);
            return gridCell.row - 1 + gridCell.rowSpan;
        } else {
            return -1;
        }
    }

    private int getSpannedRowCount() {
        return firstChildPositionForRow.size();
    }

    private int getNextSpannedRow(int rowIndex) {
        int firstPositionInRow = getFirstPositionInRowGroup(rowIndex);
        int nextRow = rowIndex + 1;
        while (nextRow < getSpannedRowCount()
                && getFirstPositionInRowGroup(nextRow) == firstPositionInRow) {
            nextRow++;
        }
        return nextRow;
    }

    private int getPreviousSpannedRow(int rowIndex) {
        int firstPositionInRow = getFirstPositionInRowGroup(rowIndex);
        int previousRow = rowIndex - 1;
        while (previousRow > 0 && getFirstPositionInRowGroup(previousRow) == firstPositionInRow) {
            previousRow--;
        }
        return previousRow;
    }

    private int getFirstPositionInRowGroup(int rowIndex) {
        return firstChildPositionForRow.get(rowIndex);
    }

    private int getLastPositionInRowGroup(final int rowIndex, RecyclerView.State state) {
        int nextRow = getNextSpannedRow(rowIndex);
        return (nextRow != getSpannedRowCount()) ? // check if reached boundary
                getFirstPositionInRowGroup(nextRow) - 1
                : state.getItemCount() - 1;
    }

    /**
     * Lay out a given 'row'. We might actually add more that one row if the requested row contains
     * a row-spanning cell. Returns the pixel height of the rows laid out.
     * <p>
     * To simplify logic & book-keeping, views are attached in adapter order, that is child 0 will
     * always be the earliest position displayed etc.
     *
     * @param startY starting y coord for new row. If startTop is true then it will be the y coord
     *               for the top of the row, otherwise the bottom.
     */
    private int layoutRowGroup(
            int rowIndex, int startY, boolean startTop, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // Record the positions of the first and last items for the algorithm to use later on
        final int firstPositionInRowGroup = getFirstPositionInRowGroup(rowIndex);
        final int lastPositionInRowGroup = getLastPositionInRowGroup(rowIndex, state);

        // Record the top row of the group to use later on
        final int topRowInGroup = cells.get(firstPositionInRowGroup).row;

        // This flag may get set in the loops below
        boolean containsRemovedItems = false;

        // The layout algorithm runs two passes over the cells (and the corresponding views) in
        // this row group: The purpose of the first pass is to determine the vertical boundaries of
        // each row when all cells in the group are allowed to flex to wrap their contents. The
        // purpose of the second pass is to layout the views in their final positions according to
        // these row boundaries.

        // Array of row boundaries which are the start/end positions of rows in this group relative
        // to the top of the row group which is `rowBoundaries.get(0) = 0` (note that a
        // SparseIntArray returns `0` for a key which does not exist).
        // The top of row i relative to the top of the row group is `rowBoundaries.get(i)` and the
        // position of the bottom of that row is `rowBoundaries.get(i + 1)`.
        final SparseIntArray rowBoundaries = new SparseIntArray();

        // TODO stop using these arrays
        final List<View> rowGroupViews = new ArrayList<>();
        final List<GridCell> rowGroupCells = new ArrayList<>();

        // FIRST PASS: MEASURE
        int insertPosition = (rowIndex < firstVisibleRow) ? 0 : getChildCount();
        for (int position = firstPositionInRowGroup;
             position <= lastPositionInRowGroup;
             position++, insertPosition++) {
            final View view = recycler.getViewForPosition(position);
            final GridCell cell = cells.get(position);

            rowGroupViews.add(view);
            rowGroupCells.add(cell);

            final LayoutParams lp = (LayoutParams) view.getLayoutParams();
            containsRemovedItems |= lp.isItemRemoved();
            addView(view, insertPosition);

            // TODO use orientation helper
            // Width is exact, fitting to column boundaries:
            final int wSpec = getChildMeasureSpec(
                    cellBorders[cell.column + cell.columnSpan] - cellBorders[cell.column],
                    View.MeasureSpec.EXACTLY,
                    0,
                    lp.width,
                    false
            );
            // In this first pass, height is unspecified, we see how tall each cell needs to be
            // to wrap its contents:
            final int hSpec = getChildMeasureSpec(
                    getHeight(),
                    View.MeasureSpec.UNSPECIFIED,
                    0,
                    LayoutParams.WRAP_CONTENT,
                    true
            );
            measureChildWithDecorationsAndMargin(view, wSpec, hSpec);

            // Update rowBoundaries, note topBoundary will be 0 if cell is in first row of this
            // row group (rowBoundaries is zero-filled above).
            final int topBoundaryIndex = cell.row - topRowInGroup;
            final int bottomBoundaryIndex = topBoundaryIndex + cell.rowSpan;
            final int topBoundary = rowBoundaries.get(topBoundaryIndex);
            final int bottomBoundary = topBoundary + getDecoratedMeasuredHeight(view);
            if (bottomBoundary > rowBoundaries.get(bottomBoundaryIndex)) {
                rowBoundaries.put(bottomBoundaryIndex, bottomBoundary);
            }
        }

        final int groupBottomBoundaryIndex = rowBoundaries.keyAt(rowBoundaries.size() - 1);
        final int groupBottomBoundary = rowBoundaries.get(groupBottomBoundaryIndex);

        // SECOND PASS: LAYOUT
        for (int i = 0; i < rowGroupViews.size(); i++) {
            final View view = rowGroupViews.get(i);
            final GridCell cell = rowGroupCells.get(i);
            final LayoutParams lp = (LayoutParams) view.getLayoutParams();

            // left, width, right are easy again because column positions are fixed:
            final int left = cellBorders[cell.column] + lp.leftMargin;
            final int width = getDecoratedMeasuredWidth(view);
            final int right = left + width;

            // Height comes from the rowBoundary info obtained in the measure pass above:
            final int topBoundaryIndex = cell.row - topRowInGroup;
            final int bottomBoundaryIndex = topBoundaryIndex + cell.rowSpan;
            final int height = rowBoundaries.get(bottomBoundaryIndex) - rowBoundaries.get(topBoundaryIndex);

            // Now work out top and bottom, which is calculated first depends on whether we're
            // laying out in the downwards direction (startTop == true) or an upwards direction
            // (startTop == false):
            final int top;
            final int bottom;
            if (startTop) {
                top = startY + lp.topMargin + rowBoundaries.get(topBoundaryIndex);
                bottom = top + height;
            } else {
                // How far is the bottom of this cell from the very bottom of the whole row group?
                final int bottomBoundaryOffset = groupBottomBoundary - rowBoundaries.get(bottomBoundaryIndex);
                bottom = startY - lp.bottomMargin - bottomBoundaryOffset;
                top = bottom - height;
            }

            // Re-measure child view so its children can measure themselves correctly
            final int wSpec = getChildMeasureSpec(width, View.MeasureSpec.EXACTLY,0, LayoutParams.MATCH_PARENT,false);
            final int hSpec = getChildMeasureSpec(height, View.MeasureSpec.EXACTLY,0, LayoutParams.MATCH_PARENT,true);
            measureChildWithDecorationsAndMargin(view, wSpec, hSpec);

            // Finally, layout the view in its final position!
            layoutDecorated(view, left, top, right, bottom);

            // Book-keeping
            lp.columnSpan = cell.columnSpan;
            lp.rowSpan = cell.rowSpan;
        }

        // This is just book-keeping
        if (firstPositionInRowGroup < firstVisiblePosition) {
            firstVisiblePosition = firstPositionInRowGroup;
            firstVisibleRow = getRowTopIndex(firstVisiblePosition);
        }
        if (lastPositionInRowGroup > lastVisiblePosition) {
            lastVisiblePosition = lastPositionInRowGroup;
            lastVisibleRow = getRowBottomIndex(lastVisiblePosition);
        }

        // Return position of the bottom of this row group
        if (containsRemovedItems) {
            return 0; // don't consume space for rows with disappearing items
        } else {
            return groupBottomBoundary;
        }
    }

    /**
     * Remove and recycle all items in this 'row'. If the row includes a row-spanning cell then all
     * cells in the spanned rows will be removed.
     */
    private void recycleRow(
            int rowIndex, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int firstPositionInRow = getFirstPositionInRowGroup(rowIndex);
        int lastPositionInRow = getLastPositionInRowGroup(rowIndex, state);
        int toRemove = lastPositionInRow;
        while (toRemove >= firstPositionInRow) {
            int index = toRemove - firstVisiblePosition;
            removeAndRecycleViewAt(index, recycler);
            toRemove--;
        }
        if (rowIndex == firstVisibleRow) {
            firstVisiblePosition = lastPositionInRow + 1;
            firstVisibleRow = getRowTopIndex(firstVisiblePosition);
        }
        if (rowIndex == lastVisibleRow) {
            lastVisiblePosition = firstPositionInRow - 1;
            lastVisibleRow = getRowBottomIndex(lastVisiblePosition);
        }
    }

    private void layoutDisappearingViews(
            RecyclerView.Recycler recycler, RecyclerView.State state, int startTop) {
        // TODO
    }

    private void calculateWindowSize() {
        // TODO use OrientationHelper#getTotalSpace
        int cellWidth =
                (int) Math.floor((getWidth() - getPaddingLeft() - getPaddingRight()) / columns);
        cellHeight = (int) Math.floor(cellWidth * (1f / cellAspectRatio)) + additionalCellHeight;
        calculateCellBorders();
    }

    private void reset() {
        cells = null;
        firstChildPositionForRow = null;
        firstVisiblePosition = 0;
        firstVisibleRow = 0;
        lastVisiblePosition = 0;
        lastVisibleRow = 0;
        cellHeight = 0;
        forceClearOffsets = false;
    }

    private void resetVisibleItemTracking() {
        // maintain the firstVisibleRow but reset other state vars
        // TODO make orientation agnostic
        int minimumVisibleRow = getMinimumFirstVisibleRow();
        if (firstVisibleRow > minimumVisibleRow) firstVisibleRow = minimumVisibleRow;
        firstVisiblePosition = getFirstPositionInRowGroup(firstVisibleRow);
        lastVisibleRow = firstVisibleRow;
        lastVisiblePosition = firstVisiblePosition;
    }

    private int getMinimumFirstVisibleRow() {
        int maxDisplayedRows = (int) Math.ceil((float) getHeight() / cellHeight) + 1;
        if (totalRows < maxDisplayedRows) return 0;
        int minFirstRow = totalRows - maxDisplayedRows;
        // adjust to spanned rows
        return getRowTopIndex(getFirstPositionInRowGroup(minFirstRow));
    }

    /* Adapted from GridLayoutManager */

    private void calculateCellBorders() {
        cellBorders = new int[columns + 1];
        int totalSpace = getWidth() - getPaddingLeft() - getPaddingRight();
        int consumedPixels = getPaddingLeft();
        cellBorders[0] = consumedPixels;
        int sizePerSpan = totalSpace / columns;
        int sizePerSpanRemainder = totalSpace % columns;
        int additionalSize = 0;
        for (int i = 1; i <= columns; i++) {
            int itemSize = sizePerSpan;
            additionalSize += sizePerSpanRemainder;
            if (additionalSize > 0 && (columns - additionalSize) < sizePerSpanRemainder) {
                itemSize += 1;
                additionalSize -= columns;
            }
            consumedPixels += itemSize;
            cellBorders[i] = consumedPixels;
        }
    }

    private void measureChildWithDecorationsAndMargin(View child, int widthSpec, int heightSpec) {
        calculateItemDecorationsForChild(child, itemDecorationInsets);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        widthSpec = updateSpecWithExtra(widthSpec, lp.leftMargin + itemDecorationInsets.left,
                lp.rightMargin + itemDecorationInsets.right);
        heightSpec = updateSpecWithExtra(heightSpec, lp.topMargin + itemDecorationInsets.top,
                lp.bottomMargin + itemDecorationInsets.bottom);
        child.measure(widthSpec, heightSpec);
    }

    private int updateSpecWithExtra(int spec, int startInset, int endInset) {
        if (startInset == 0 && endInset == 0) {
            return spec;
        }
        int mode = View.MeasureSpec.getMode(spec);
        if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.EXACTLY) {
            return View.MeasureSpec.makeMeasureSpec(
                    View.MeasureSpec.getSize(spec) - startInset - endInset, mode);
        }
        return spec;
    }

    /* Adapted from ConstraintLayout */

    private void parseAspectRatio(String aspect) {
        if (aspect != null) {
            int colonIndex = aspect.indexOf(':');
            if (colonIndex >= 0 && colonIndex < aspect.length() - 1) {
                String nominator = aspect.substring(0, colonIndex);
                String denominator = aspect.substring(colonIndex + 1);
                if (nominator.length() > 0 && denominator.length() > 0) {
                    try {
                        float nominatorValue = Float.parseFloat(nominator);
                        float denominatorValue = Float.parseFloat(denominator);
                        if (nominatorValue > 0 && denominatorValue > 0) {
                            cellAspectRatio = Math.abs(nominatorValue / denominatorValue);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        // Ignore
                    }
                }
            }
        }
        throw new IllegalArgumentException("Could not parse aspect ratio: '" + aspect + "'");
    }

}
