package com.elevasign.player.domain.usecase;

import android.content.Context;
import com.elevasign.player.data.local.datastore.PlayerPreferences;
import com.elevasign.player.data.local.file.MediaFileManager;
import com.elevasign.player.data.repository.PlayerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class HandleCommandUseCase_Factory implements Factory<HandleCommandUseCase> {
  private final Provider<Context> contextProvider;

  private final Provider<PlayerRepository> repositoryProvider;

  private final Provider<PlayerPreferences> prefsProvider;

  private final Provider<MediaFileManager> fileManagerProvider;

  private final Provider<SyncManifestUseCase> syncManifestProvider;

  public HandleCommandUseCase_Factory(Provider<Context> contextProvider,
      Provider<PlayerRepository> repositoryProvider, Provider<PlayerPreferences> prefsProvider,
      Provider<MediaFileManager> fileManagerProvider,
      Provider<SyncManifestUseCase> syncManifestProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
    this.fileManagerProvider = fileManagerProvider;
    this.syncManifestProvider = syncManifestProvider;
  }

  @Override
  public HandleCommandUseCase get() {
    return newInstance(contextProvider.get(), repositoryProvider.get(), prefsProvider.get(), fileManagerProvider.get(), syncManifestProvider.get());
  }

  public static HandleCommandUseCase_Factory create(Provider<Context> contextProvider,
      Provider<PlayerRepository> repositoryProvider, Provider<PlayerPreferences> prefsProvider,
      Provider<MediaFileManager> fileManagerProvider,
      Provider<SyncManifestUseCase> syncManifestProvider) {
    return new HandleCommandUseCase_Factory(contextProvider, repositoryProvider, prefsProvider, fileManagerProvider, syncManifestProvider);
  }

  public static HandleCommandUseCase newInstance(Context context, PlayerRepository repository,
      PlayerPreferences prefs, MediaFileManager fileManager, SyncManifestUseCase syncManifest) {
    return new HandleCommandUseCase(context, repository, prefs, fileManager, syncManifest);
  }
}
