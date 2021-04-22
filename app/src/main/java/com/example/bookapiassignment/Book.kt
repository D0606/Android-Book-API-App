package com.example.bookapiassignment




data class Book(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
) {
    data class Item(
        val volumeInfo: VolumeInfo
    ) {
        data class VolumeInfo(
            val allowAnonLogging: Boolean,
            val authors: List<String>?,
            val averageRating: Float,
            val canonicalVolumeLink: String,
            val categories: List<String>?,
            val contentVersion: String,
            val description: String?,
            val imageLinks: ImageLinks?,
            val industryIdentifiers: List<IndustryIdentifier>?,
            val infoLink: String,
            val language: String,
            val maturityRating: String,
            val pageCount: Int,
            val panelizationSummary: PanelizationSummary,
            val previewLink: String,
            val printType: String,
            val publishedDate: String,
            val publisher: String?,
            val ratingsCount: Int,
            val readingModes: ReadingModes,
            val subtitle: String,
            val title: String
        ) {
            data class ImageLinks(
                val smallThumbnail: String,
                val thumbnail: String
            )

            data class IndustryIdentifier(
                val identifier: String,
                val type: String
            )

            data class PanelizationSummary(
                val containsEpubBubbles: Boolean,
                val containsImageBubbles: Boolean
            )

            data class ReadingModes(
                val image: Boolean,
                val text: Boolean
            )
        }
    }
}