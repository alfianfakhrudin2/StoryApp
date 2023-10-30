package com.example.storyapp.utils

import com.example.storyapp.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val listStoryItem = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1683497415212_dnKjbXF-.jpg",
                "2023-05-07T22:10:15.213Z",
                "Alfian Ganteng",
                "Halo",
                37.422092,
                "story-$i",
                -122.08392
            )
            items.add(listStoryItem)
        }
        return items
    }
}