package com.example.minimallauncher.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.favoritesDataStore by preferencesDataStore(
    name = "favorites"
)

class FavoritesRepository(
    context: Context
) {
    private val dataStore = context.applicationContext.favoritesDataStore

    val favoritePackageNames: Flow<List<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            decodePackageNames(preferences[FAVORITE_PACKAGES])
        }

    suspend fun addFavorite(packageName: String) {
        updateFavorites { currentFavorites ->
            if (packageName in currentFavorites) {
                currentFavorites
            } else {
                currentFavorites + packageName
            }
        }
    }

    suspend fun removeFavorite(packageName: String) {
        updateFavorites { currentFavorites ->
            currentFavorites.filterNot { it == packageName }
        }
    }

    private suspend fun updateFavorites(
        transform: (List<String>) -> List<String>
    ) {
        dataStore.edit { preferences ->
            val currentFavorites = decodePackageNames(
                preferences[FAVORITE_PACKAGES]
            )
            preferences[FAVORITE_PACKAGES] = transform(currentFavorites)
                .distinct()
                .joinToString(PACKAGE_SEPARATOR)
        }
    }

    private fun decodePackageNames(value: String?): List<String> {
        return value
            ?.split(PACKAGE_SEPARATOR)
            ?.filter { it.isNotBlank() }
            ?.distinct()
            .orEmpty()
    }

    private companion object {
        val FAVORITE_PACKAGES: Preferences.Key<String> =
            stringPreferencesKey("favorite_packages")
        const val PACKAGE_SEPARATOR = "\n"
    }
}
