package ru.blackmirrror.map.presentation.create

import ru.blackmirrror.map.domain.Category
import java.io.File

sealed class CreateMarkEvent {
    object Create: CreateMarkEvent()
    data class EditImageFile(val file: File): CreateMarkEvent()
    data class EditTitle(val title: String): CreateMarkEvent()
    data class EditDescription(val description: String): CreateMarkEvent()
    data class EditRating(val rating: Int): CreateMarkEvent()
    data class CategorySelected(val category: Category): CreateMarkEvent()
}