package com.elevasign.player.domain.usecase;

import com.elevasign.player.data.local.datastore.PlayerPreferences;
import com.elevasign.player.data.repository.PlayerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SyncManifestUseCase_Factory implements Factory<SyncManifestUseCase> {
  private final Provider<PlayerRepository> repositoryProvider;

  private final Provider<PlayerPreferences> prefsProvider;

  public SyncManifestUseCase_Factory(Provider<PlayerRepository> repositoryProvider,
      Provider<PlayerPreferences> prefsProvider) {
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public SyncManifestUseCase get() {
    return newInstance(repositoryProvider.get(), prefsProvider.get());
  }

  public static SyncManifestUseCase_Factory create(Provider<PlayerRepository> repositoryProvider,
      Provider<PlayerPreferences> prefsProvider) {
    return new SyncManifestUseCase_Factory(repositoryProvider, prefsProvider);
  }

  public static SyncManifestUseCase newInstance(PlayerRepository repository,
      PlayerPreferences prefs) {
    return new SyncManifestUseCase(repository, prefs);
  }
}
