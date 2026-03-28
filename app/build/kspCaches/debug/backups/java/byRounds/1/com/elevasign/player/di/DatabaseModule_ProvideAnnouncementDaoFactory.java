package com.elevasign.player.di;

import com.elevasign.player.data.local.db.AppDatabase;
import com.elevasign.player.data.local.db.dao.AnnouncementDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAnnouncementDaoFactory implements Factory<AnnouncementDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideAnnouncementDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AnnouncementDao get() {
    return provideAnnouncementDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAnnouncementDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideAnnouncementDaoFactory(dbProvider);
  }

  public static AnnouncementDao provideAnnouncementDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAnnouncementDao(db));
  }
}
