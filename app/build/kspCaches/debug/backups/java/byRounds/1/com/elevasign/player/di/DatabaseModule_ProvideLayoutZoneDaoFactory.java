package com.elevasign.player.di;

import com.elevasign.player.data.local.db.AppDatabase;
import com.elevasign.player.data.local.db.dao.LayoutZoneDao;
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
public final class DatabaseModule_ProvideLayoutZoneDaoFactory implements Factory<LayoutZoneDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideLayoutZoneDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public LayoutZoneDao get() {
    return provideLayoutZoneDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideLayoutZoneDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideLayoutZoneDaoFactory(dbProvider);
  }

  public static LayoutZoneDao provideLayoutZoneDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideLayoutZoneDao(db));
  }
}
