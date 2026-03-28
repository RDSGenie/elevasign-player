package com.elevasign.player.ui.player;

import com.elevasign.player.data.local.datastore.PlayerPreferences;
import com.elevasign.player.data.local.db.dao.AnnouncementDao;
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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<PlayerRepository> repositoryProvider;

  private final Provider<AnnouncementDao> announcementDaoProvider;

  private final Provider<PlayerPreferences> prefsProvider;

  public PlayerViewModel_Factory(Provider<PlayerRepository> repositoryProvider,
      Provider<AnnouncementDao> announcementDaoProvider,
      Provider<PlayerPreferences> prefsProvider) {
    this.repositoryProvider = repositoryProvider;
    this.announcementDaoProvider = announcementDaoProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(repositoryProvider.get(), announcementDaoProvider.get(), prefsProvider.get());
  }

  public static PlayerViewModel_Factory create(Provider<PlayerRepository> repositoryProvider,
      Provider<AnnouncementDao> announcementDaoProvider,
      Provider<PlayerPreferences> prefsProvider) {
    return new PlayerViewModel_Factory(repositoryProvider, announcementDaoProvider, prefsProvider);
  }

  public static PlayerViewModel newInstance(PlayerRepository repository,
      AnnouncementDao announcementDao, PlayerPreferences prefs) {
    return new PlayerViewModel(repository, announcementDao, prefs);
  }
}
