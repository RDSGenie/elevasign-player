package com.elevasign.player;

import com.elevasign.player.data.local.datastore.PlayerPreferences;
import com.elevasign.player.domain.usecase.SyncManifestUseCase;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<PlayerPreferences> prefsProvider;

  private final Provider<SyncManifestUseCase> syncManifestProvider;

  public MainActivity_MembersInjector(Provider<PlayerPreferences> prefsProvider,
      Provider<SyncManifestUseCase> syncManifestProvider) {
    this.prefsProvider = prefsProvider;
    this.syncManifestProvider = syncManifestProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<PlayerPreferences> prefsProvider,
      Provider<SyncManifestUseCase> syncManifestProvider) {
    return new MainActivity_MembersInjector(prefsProvider, syncManifestProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPrefs(instance, prefsProvider.get());
    injectSyncManifest(instance, syncManifestProvider.get());
  }

  @InjectedFieldSignature("com.elevasign.player.MainActivity.prefs")
  public static void injectPrefs(MainActivity instance, PlayerPreferences prefs) {
    instance.prefs = prefs;
  }

  @InjectedFieldSignature("com.elevasign.player.MainActivity.syncManifest")
  public static void injectSyncManifest(MainActivity instance, SyncManifestUseCase syncManifest) {
    instance.syncManifest = syncManifest;
  }
}
