package com.elevasign.player.data.local.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.elevasign.player.data.local.db.dao.AnnouncementDao;
import com.elevasign.player.data.local.db.dao.AnnouncementDao_Impl;
import com.elevasign.player.data.local.db.dao.LayoutZoneDao;
import com.elevasign.player.data.local.db.dao.LayoutZoneDao_Impl;
import com.elevasign.player.data.local.db.dao.MediaItemDao;
import com.elevasign.player.data.local.db.dao.MediaItemDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile MediaItemDao _mediaItemDao;

  private volatile AnnouncementDao _announcementDao;

  private volatile LayoutZoneDao _layoutZoneDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `media_items` (`media_id` TEXT NOT NULL, `file_url` TEXT, `file_type` TEXT NOT NULL, `display_duration_seconds` INTEGER NOT NULL, `sort_order` INTEGER NOT NULL, `local_path` TEXT, `playlist_id` TEXT NOT NULL, `playlist_name` TEXT NOT NULL, PRIMARY KEY(`media_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `announcements` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `body` TEXT NOT NULL, `display_type` TEXT NOT NULL, `bg_color` TEXT NOT NULL, `text_color` TEXT NOT NULL, `priority` INTEGER NOT NULL, `starts_at` TEXT NOT NULL, `expires_at` TEXT, `is_active` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `layout_zones` (`id` TEXT NOT NULL, `zone_name` TEXT NOT NULL, `zone_type` TEXT NOT NULL, `playlist_id` TEXT, `position_x_percent` REAL NOT NULL, `position_y_percent` REAL NOT NULL, `width_percent` REAL NOT NULL, `height_percent` REAL NOT NULL, `z_index` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '585b18f13191bd0bf5be547b5adf7c6e')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `media_items`");
        db.execSQL("DROP TABLE IF EXISTS `announcements`");
        db.execSQL("DROP TABLE IF EXISTS `layout_zones`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMediaItems = new HashMap<String, TableInfo.Column>(8);
        _columnsMediaItems.put("media_id", new TableInfo.Column("media_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("file_url", new TableInfo.Column("file_url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("file_type", new TableInfo.Column("file_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("display_duration_seconds", new TableInfo.Column("display_duration_seconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("sort_order", new TableInfo.Column("sort_order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("local_path", new TableInfo.Column("local_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("playlist_id", new TableInfo.Column("playlist_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItems.put("playlist_name", new TableInfo.Column("playlist_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMediaItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMediaItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMediaItems = new TableInfo("media_items", _columnsMediaItems, _foreignKeysMediaItems, _indicesMediaItems);
        final TableInfo _existingMediaItems = TableInfo.read(db, "media_items");
        if (!_infoMediaItems.equals(_existingMediaItems)) {
          return new RoomOpenHelper.ValidationResult(false, "media_items(com.elevasign.player.data.local.db.entity.MediaItemEntity).\n"
                  + " Expected:\n" + _infoMediaItems + "\n"
                  + " Found:\n" + _existingMediaItems);
        }
        final HashMap<String, TableInfo.Column> _columnsAnnouncements = new HashMap<String, TableInfo.Column>(10);
        _columnsAnnouncements.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("body", new TableInfo.Column("body", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("display_type", new TableInfo.Column("display_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("bg_color", new TableInfo.Column("bg_color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("text_color", new TableInfo.Column("text_color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("priority", new TableInfo.Column("priority", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("starts_at", new TableInfo.Column("starts_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("expires_at", new TableInfo.Column("expires_at", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAnnouncements.put("is_active", new TableInfo.Column("is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAnnouncements = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAnnouncements = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAnnouncements = new TableInfo("announcements", _columnsAnnouncements, _foreignKeysAnnouncements, _indicesAnnouncements);
        final TableInfo _existingAnnouncements = TableInfo.read(db, "announcements");
        if (!_infoAnnouncements.equals(_existingAnnouncements)) {
          return new RoomOpenHelper.ValidationResult(false, "announcements(com.elevasign.player.data.local.db.entity.AnnouncementEntity).\n"
                  + " Expected:\n" + _infoAnnouncements + "\n"
                  + " Found:\n" + _existingAnnouncements);
        }
        final HashMap<String, TableInfo.Column> _columnsLayoutZones = new HashMap<String, TableInfo.Column>(9);
        _columnsLayoutZones.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayoutZones.put("zone_name", new TableInfo.Column("zone_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayoutZones.put("zone_type", new TableInfo.Column("zone_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayoutZones.put("playlist_id", new TableInfo.Column("playlist_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayoutZones.put("position_x_percent", new TableInfo.Column("position_x_percent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayoutZones.put("position_y_percent", new TableInfo.Column("position_y_percent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayoutZones.put("width_percent", new TableInfo.Column("width_percent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayoutZones.put("height_percent", new TableInfo.Column("height_percent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLayoutZones.put("z_index", new TableInfo.Column("z_index", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLayoutZones = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLayoutZones = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLayoutZones = new TableInfo("layout_zones", _columnsLayoutZones, _foreignKeysLayoutZones, _indicesLayoutZones);
        final TableInfo _existingLayoutZones = TableInfo.read(db, "layout_zones");
        if (!_infoLayoutZones.equals(_existingLayoutZones)) {
          return new RoomOpenHelper.ValidationResult(false, "layout_zones(com.elevasign.player.data.local.db.entity.LayoutZoneEntity).\n"
                  + " Expected:\n" + _infoLayoutZones + "\n"
                  + " Found:\n" + _existingLayoutZones);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "585b18f13191bd0bf5be547b5adf7c6e", "0e10cf677ff49ac18daac358143d23cf");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "media_items","announcements","layout_zones");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `media_items`");
      _db.execSQL("DELETE FROM `announcements`");
      _db.execSQL("DELETE FROM `layout_zones`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MediaItemDao.class, MediaItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AnnouncementDao.class, AnnouncementDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LayoutZoneDao.class, LayoutZoneDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MediaItemDao mediaItemDao() {
    if (_mediaItemDao != null) {
      return _mediaItemDao;
    } else {
      synchronized(this) {
        if(_mediaItemDao == null) {
          _mediaItemDao = new MediaItemDao_Impl(this);
        }
        return _mediaItemDao;
      }
    }
  }

  @Override
  public AnnouncementDao announcementDao() {
    if (_announcementDao != null) {
      return _announcementDao;
    } else {
      synchronized(this) {
        if(_announcementDao == null) {
          _announcementDao = new AnnouncementDao_Impl(this);
        }
        return _announcementDao;
      }
    }
  }

  @Override
  public LayoutZoneDao layoutZoneDao() {
    if (_layoutZoneDao != null) {
      return _layoutZoneDao;
    } else {
      synchronized(this) {
        if(_layoutZoneDao == null) {
          _layoutZoneDao = new LayoutZoneDao_Impl(this);
        }
        return _layoutZoneDao;
      }
    }
  }
}
