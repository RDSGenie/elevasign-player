package com.elevasign.player.data.remote.download;

import com.elevasign.player.data.local.file.MediaFileManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class MediaDownloader_Factory implements Factory<MediaDownloader> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<MediaFileManager> mediaFileManagerProvider;

  public MediaDownloader_Factory(Provider<OkHttpClient> okHttpClientProvider,
      Provider<MediaFileManager> mediaFileManagerProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
    this.mediaFileManagerProvider = mediaFileManagerProvider;
  }

  @Override
  public MediaDownloader get() {
    return newInstance(okHttpClientProvider.get(), mediaFileManagerProvider.get());
  }

  public static MediaDownloader_Factory create(Provider<OkHttpClient> okHttpClientProvider,
      Provider<MediaFileManager> mediaFileManagerProvider) {
    return new MediaDownloader_Factory(okHttpClientProvider, mediaFileManagerProvider);
  }

  public static MediaDownloader newInstance(OkHttpClient okHttpClient,
      MediaFileManager mediaFileManager) {
    return new MediaDownloader(okHttpClient, mediaFileManager);
  }
}
