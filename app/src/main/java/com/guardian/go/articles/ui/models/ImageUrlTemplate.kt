package com.guardian.go.articles.ui.models

/**
 * MAPI does not return image urls to the apps but url templates, from which the app can construct an appropriate url
 * for an appropriate sized image in a given context.
 *
 *
 * This class represents one of those templates.
 */
class ImageUrlTemplate(private val template: String) {

    private fun getUrl(width: Int, quality: Int): String {
        return template.replace(WIDTH_PARAMETER, width.toString())
                .replace(HEIGHT_PARAMETER, HYPHEN)
                .replace(QUALITY_PARAMETER, quality.toString())
    }

    val iconTinySizeUrl: String
        get() = getUrl(ICON_TINY_WIDTH, DESIRED_QUALITY)

    val iconSmallSizeUrl: String
        get() = getUrl(ICON_SMALL_WIDTH, DESIRED_QUALITY)

    val iconMediumSizeUrl: String
        get() = getUrl(ICON_MEDIUM_WIDTH, DESIRED_QUALITY)

    val iconLargeSizeUrl: String
        get() = getUrl(ICON_LARGE_WIDTH, DESIRED_QUALITY)

    val smallUrl: String
        get() = getUrl(SMALL_WIDTH, DESIRED_QUALITY)

    val widgetSizeUrl: String
        get() = getUrl(WIDGET_SIZE, DESIRED_QUALITY)

    val mediumUrl: String
        get() = getUrl(MEDIUM_WIDTH, DESIRED_QUALITY)

    val largeUrl: String
        get() = getUrl(LARGE_WIDTH, DESIRED_QUALITY)

    companion object {
        private const val SMALL_WIDTH = 450
        private const val HYPHEN = "-"
        private const val ICON_TINY_WIDTH = 40
        private const val ICON_SMALL_WIDTH = 60
        private const val ICON_MEDIUM_WIDTH = 80
        private const val ICON_LARGE_WIDTH = 160
        private const val DESIRED_QUALITY = 95
        private const val WIDGET_SIZE = 150
        private const val MEDIUM_WIDTH = 750
        private const val LARGE_WIDTH = 900
        private const val WIDTH_PARAMETER = "#{width}"
        private const val HEIGHT_PARAMETER = "#{height}"
        private const val QUALITY_PARAMETER = "#{quality}"
    }
}
