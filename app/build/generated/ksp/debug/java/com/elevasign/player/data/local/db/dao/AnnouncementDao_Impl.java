package com.elevasign.player.data.local.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.elevasign.player.data.local.db.entity.AnnouncementEntity;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AnnouncementDao_Impl implements AnnouncementDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AnnouncementEntity> __insertionAdapterOfAnnouncementEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public AnnouncementDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAnnouncementEntity = new EntityInsertionAdapter<AnnouncementEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `announcements` (`id`,`title`,`body`,`display_type`,`bg_color`,`text_color`,`priority`,`starts_at`,`expires_at`,`is_active`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AnnouncementEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getBody());
        statement.bindString(4, entity.getDisplayType());
        statement.bindString(5, entity.getBgColor());
        statement.bindString(6, entity.getTextColor());
        statement.bindLong(7, entity.getPriority());
        statement.bindString(8, entity.getStartsAt());
        if (entity.getExpiresAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getExpiresAt());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(10, _tmp);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM announcements";
        return _query;
      }
    };
  }

  @Override
  public Object upsertAll(final List<AnnouncementEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAnnouncementEntity.insert(items);
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
  public Flow<List<AnnouncementEntity>> observeActive(final String nowIso) {
    final String _sql = "\n"
            + "        SELECT * FROM announcements\n"
            + "        WHERE is_active = 1\n"
            + "          AND starts_at <= ?\n"
            + "          AND (expires_at IS NULL OR expires_at > ?)\n"
            + "        ORDER BY priority DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, nowIso);
    _argIndex = 2;
    _statement.bindString(_argIndex, nowIso);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"announcements"}, new Callable<List<AnnouncementEntity>>() {
      @Override
      @NonNull
      public List<AnnouncementEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfDisplayType = CursorUtil.getColumnIndexOrThrow(_cursor, "display_type");
          final int _cursorIndexOfBgColor = CursorUtil.getColumnIndexOrThrow(_cursor, "bg_color");
          final int _cursorIndexOfTextColor = CursorUtil.getColumnIndexOrThrow(_cursor, "text_color");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfStartsAt = CursorUtil.getColumnIndexOrThrow(_cursor, "starts_at");
          final int _cursorIndexOfExpiresAt = CursorUtil.getColumnIndexOrThrow(_cursor, "expires_at");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
          final List<AnnouncementEntity> _result = new ArrayList<AnnouncementEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AnnouncementEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final String _tmpDisplayType;
            _tmpDisplayType = _cursor.getString(_cursorIndexOfDisplayType);
            final String _tmpBgColor;
            _tmpBgColor = _cursor.getString(_cursorIndexOfBgColor);
            final String _tmpTextColor;
            _tmpTextColor = _cursor.getString(_cursorIndexOfTextColor);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final String _tmpStartsAt;
            _tmpStartsAt = _cursor.getString(_cursorIndexOfStartsAt);
            final String _tmpExpiresAt;
            if (_cursor.isNull(_cursorIndexOfExpiresAt)) {
              _tmpExpiresAt = null;
            } else {
              _tmpExpiresAt = _cursor.getString(_cursorIndexOfExpiresAt);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            _item = new AnnouncementEntity(_tmpId,_tmpTitle,_tmpBody,_tmpDisplayType,_tmpBgColor,_tmpTextColor,_tmpPriority,_tmpStartsAt,_tmpExpiresAt,_tmpIsActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
