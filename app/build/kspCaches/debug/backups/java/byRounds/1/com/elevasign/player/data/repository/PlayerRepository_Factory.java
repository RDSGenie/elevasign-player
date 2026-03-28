package com.elevasign.player.data.repository;

import com.elevasign.player.data.local.datastore.PlayerPreferences;
import com.elevasign.player.data.local.db.dao.AnnouncementDao;
import com.elevasign.player.data.local.db.dao.LayoutZoneDao;
import com.elevasign.player.data.local.db.dao.MediaItemDao;
import com.elevasign.player.data.local.file.MediaFileManager;
import com.elevasign.player.data.remote.SupabaseApi;
import com.elevasign.player.data.remote.download.MediaDownloader;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PlayerRepository_Factory implements Factory<PlayerRepository> {
  private final Provider<SupabaseApi> apiProvider;

  private final Provider<MediaItemDao> mediaItemDaoProvider;

  private final Provider<AnnouncementDao> announcementDaoProvider;

  private final Provider<LayoutZoneDao> layoutZoneDaoProvider;

  private final Provider<PlayerPreferences> prefsProvider;

  private final Provider<MediaDownloader> downloaderProvider;

  private final Provider<MediaFileManager> fileManagerProvider;

  public PlayerRepository_Factory(Provider<SupabaseApi> apiProvider,
      Provider<MediaItemDao> mediaItemDaoProvider,
      Provider<AnnouncementDao> announcementDaoProvider,
      Provider<LayoutZoneDao> layoutZoneDaoProvider, Provider<PlayerPreferences> prefsProvider,
      Provider<MediaDownloader> downloaderProvider,
      Provider<MediaFileManager> fileManagerProvider) {
    this.apiProvider = apiProvider;
    this.mediaItemDaoProvider = mediaItemDaoProvider;
    this.announcementDaoProvider = announcementDaoProvider;
    this.layoutZoneDaoProvider = layoutZoneDaoProvider;
    this.prefsProvider = prefsProvider;
    this.downloaderProvider = downloaderProvider;
    this.fileManagerProvider = fileManagerProvider;
  }

  @Override
  public PlayerRepository get() {
    return newInstance(apiProvider.get(), mediaItemDaoProvider.get(), announcementDaoProvider.get(), layoutZoneDaoProvider.get(), prefsProvider.get(), downloaderProvider.get(), fileManagerProvider.get());
  }

  public static PlayerRepository_Factory create(Provider<SupabaseApi> apiProvider,
      Provider<MediaItemDao> mediaItemDaoProvider,
      Provider<AnnouncementDao> announcementDaoProvider,
      Provider<LayoutZoneDao> layoutZoneDaoProvider, Provider<PlayerPreferences> prefsProvider,
      Provider<MediaDownloader> downloaderProvider,
      Provider<MediaFileManager> fileManagerProvider) {
    return new PlayerRepository_Factory(apiProvider, mediaItemDaoProvider, announcementDaoProvider, layoutZoneDaoProvider, prefsProvider, downloaderProvider, fileManagerProvider);
  }

  public static PlayerRepository newInstance(SupabaseApi api, MediaItemDao mediaItemDao,
      AnnouncementDao announcementDao, LayoutZoneDao layoutZoneDao, PlayerPreferences prefs,
      MediaDownloader downloader, MediaFileManager fileManager) {
    return new PlayerRepository(api, mediaItemDao, announcementDao, layoutZoneDao, prefs, downloader, fileManager);
  }
}
