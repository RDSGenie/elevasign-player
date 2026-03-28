package com.elevasign.player.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.elevasign.player.data.local.datastore.PlayerPreferences;
import com.elevasign.player.data.local.file.MediaFileManager;
import com.elevasign.player.data.repository.PlayerRepository;
import com.elevasign.player.domain.usecase.HandleCommandUseCase;
import dagger.internal.DaggerGenerated;
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
public final class HeartbeatWorker_Factory {
  private final Provider<PlayerRepository> repositoryProvider;

  private final Provider<PlayerPreferences> prefsProvider;

  private final Provider<MediaFileManager> fileManagerProvider;

  private final Provider<HandleCommandUseCase> handleCommandProvider;

  public HeartbeatWorker_Factory(Provider<PlayerRepository> repositoryProvider,
      Provider<PlayerPreferences> prefsProvider, Provider<MediaFileManager> fileManagerProvider,
      Provider<HandleCommandUseCase> handleCommandProvider) {
    this.repositoryProvider = repositoryProvider;
    this.prefsProvider = prefsProvider;
    this.fileManagerProvider = fileManagerProvider;
    this.handleCommandProvider = handleCommandProvider;
  }

  public HeartbeatWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, repositoryProvider.get(), prefsProvider.get(), fileManagerProvider.get(), handleCommandProvider.get());
  }

  public static HeartbeatWorker_Factory create(Provider<PlayerRepository> repositoryProvider,
      Provider<PlayerPreferences> prefsProvider, Provider<MediaFileManager> fileManagerProvider,
      Provider<HandleCommandUseCase> handleCommandProvider) {
    return new HeartbeatWorker_Factory(repositoryProvider, prefsProvider, fileManagerProvider, handleCommandProvider);
  }

  public static HeartbeatWorker newInstance(Context context, WorkerParameters params,
      PlayerRepository repository, PlayerPreferences prefs, MediaFileManager fileManager,
      HandleCommandUseCase handleCommand) {
    return new HeartbeatWorker(context, params, repository, prefs, fileManager, handleCommand);
  }
}
