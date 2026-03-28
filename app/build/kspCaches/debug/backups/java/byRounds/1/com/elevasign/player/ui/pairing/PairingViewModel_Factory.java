package com.elevasign.player.ui.pairing;

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
public final class PairingViewModel_Factory implements Factory<PairingViewModel> {
  private final Provider<PlayerRepository> repositoryProvider;

  private final Provider<PlayerPreferences> prefsProvider;

  public PairingViewModel_Factory(Provider<PlayerRepository> repositoryProvider,
      Provider<PlayerPreferences> prefsProvider) {
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public PairingViewModel get() {
    return newInstance(repositoryProvider.get(), prefsProvider.get());
  }

  public static PairingViewModel_Factory create(Provider<PlayerRepository> repositoryProvider,
      Provider<PlayerPreferences> prefsProvider) {
    return new PairingViewModel_Factory(repositoryProvider, prefsProvider);
  }

  public static PairingViewModel newInstance(PlayerRepository repository, PlayerPreferences prefs) {
    return new PairingViewModel(repository, prefs);
  }
}
