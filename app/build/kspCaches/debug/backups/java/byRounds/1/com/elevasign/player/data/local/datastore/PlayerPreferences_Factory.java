package com.elevasign.player.data.local.datastore;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class PlayerPreferences_Factory implements Factory<PlayerPreferences> {
  private final Provider<Context> contextProvider;

  public PlayerPreferences_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PlayerPreferences get() {
    return newInstance(contextProvider.get());
  }

  public static PlayerPreferences_Factory create(Provider<Context> contextProvider) {
    return new PlayerPreferences_Factory(contextProvider);
  }

  public static PlayerPreferences newInstance(Context context) {
    return new PlayerPreferences(context);
  }
}
