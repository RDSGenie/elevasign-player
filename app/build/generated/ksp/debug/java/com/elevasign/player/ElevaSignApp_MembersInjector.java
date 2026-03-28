package com.elevasign.player;

import androidx.hilt.work.HiltWorkerFactory;
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
public final class ElevaSignApp_MembersInjector implements MembersInjector<ElevaSignApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public ElevaSignApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<ElevaSignApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new ElevaSignApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(ElevaSignApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.elevasign.player.ElevaSignApp.workerFactory")
  public static void injectWorkerFactory(ElevaSignApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
