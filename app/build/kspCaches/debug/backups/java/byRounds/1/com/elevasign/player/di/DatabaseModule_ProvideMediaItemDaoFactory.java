package com.elevasign.player.di;

import com.elevasign.player.data.local.db.AppDatabase;
import com.elevasign.player.data.local.db.dao.MediaItemDao;
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
public final class DatabaseModule_ProvideMediaItemDaoFactory implements Factory<MediaItemDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideMediaItemDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public MediaItemDao get() {
    return provideMediaItemDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideMediaItemDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideMediaItemDaoFactory(dbProvider);
  }

  public static MediaItemDao provideMediaItemDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMediaItemDao(db));
  }
}
