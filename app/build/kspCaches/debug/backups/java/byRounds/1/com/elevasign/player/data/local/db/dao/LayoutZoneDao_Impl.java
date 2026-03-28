package com.elevasign.player.data.local.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.elevasign.player.data.local.db.entity.LayoutZoneEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LayoutZoneDao_Impl implements LayoutZoneDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LayoutZoneEntity> __insertionAdapterOfLayoutZoneEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public LayoutZoneDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLayoutZoneEntity = new EntityInsertionAdapter<LayoutZoneEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `layout_zones` (`id`,`zone_name`,`zone_type`,`playlist_id`,`position_x_percent`,`position_y_percent`,`width_percent`,`height_percent`,`z_index`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LayoutZoneEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getZoneName());
        statement.bindString(3, entity.getZoneType());
        if (entity.getPlaylistId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPlaylistId());
        }
        statement.bindDouble(5, entity.getPositionXPercent());
        statement.bindDouble(6, entity.getPositionYPercent());
        statement.bindDouble(7, entity.getWidthPercent());
        statement.bindDouble(8, entity.getHeightPercent());
        statement.bindLong(9, entity.getZIndex());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM layout_zones";
        return _query;
      }
    };
  }

  @Override
  public Object upsertAll(final List<LayoutZoneEntity> zones,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLayoutZoneEntity.insert(zones);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<LayoutZoneEntity>> $completion) {
    final String _sql = "SELECT * FROM layout_zones ORDER BY z_index ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<LayoutZoneEntity>>() {
      @Override
      @NonNull
      public List<LayoutZoneEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfZoneName = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_name");
          final int _cursorIndexOfZoneType = CursorUtil.getColumnIndexOrThrow(_cursor, "zone_type");
          final int _cursorIndexOfPlaylistId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_id");
          final int _cursorIndexOfPositionXPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "position_x_percent");
          final int _cursorIndexOfPositionYPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "position_y_percent");
          final int _cursorIndexOfWidthPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "width_percent");
          final int _cursorIndexOfHeightPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "height_percent");
          final int _cursorIndexOfZIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "z_index");
          final List<LayoutZoneEntity> _result = new ArrayList<LayoutZoneEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LayoutZoneEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpZoneName;
            _tmpZoneName = _cursor.getString(_cursorIndexOfZoneName);
            final String _tmpZoneType;
            _tmpZoneType = _cursor.getString(_cursorIndexOfZoneType);
            final String _tmpPlaylistId;
            if (_cursor.isNull(_cursorIndexOfPlaylistId)) {
              _tmpPlaylistId = null;
            } else {
              _tmpPlaylistId = _cursor.getString(_cursorIndexOfPlaylistId);
            }
            final float _tmpPositionXPercent;
            _tmpPositionXPercent = _cursor.getFloat(_cursorIndexOfPositionXPercent);
            final float _tmpPositionYPercent;
            _tmpPositionYPercent = _cursor.getFloat(_cursorIndexOfPositionYPercent);
            final float _tmpWidthPercent;
            _tmpWidthPercent = _cursor.getFloat(_cursorIndexOfWidthPercent);
            final float _tmpHeightPercent;
            _tmpHeightPercent = _cursor.getFloat(_cursorIndexOfHeightPercent);
            final int _tmpZIndex;
            _tmpZIndex = _cursor.getInt(_cursorIndexOfZIndex);
            _item = new LayoutZoneEntity(_tmpId,_tmpZoneName,_tmpZoneType,_tmpPlaylistId,_tmpPositionXPercent,_tmpPositionYPercent,_tmpWidthPercent,_tmpHeightPercent,_tmpZIndex);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
