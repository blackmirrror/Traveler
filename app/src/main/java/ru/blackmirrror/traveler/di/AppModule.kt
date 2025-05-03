package ru.blackmirrror.traveler.di

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import ru.blackmirrror.core.api.ActivityProvider
import ru.blackmirrror.core.api.NetworkProvider
import ru.blackmirrror.traveler.network.NetworkProviderImpl

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideActivityProvider(activity: Activity): ActivityProvider {
        return object : ActivityProvider {
            override fun getActivity(): Activity {
                return activity
            }
        }
    }

    @Provides
    fun provideNetworkProvider(@ApplicationContext context: Context): NetworkProvider {
        return NetworkProviderImpl(context)
    }
}
