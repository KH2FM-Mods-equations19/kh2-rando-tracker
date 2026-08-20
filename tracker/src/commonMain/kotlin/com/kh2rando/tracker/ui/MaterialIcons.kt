package com.kh2rando.tracker.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object MaterialIcons {

  @Suppress("CheckReturnValue")
  val add: ImageVector
    get() {
      if (_add != null) {
        return _add!!
      }
      _add =
        ImageVector.Builder(
          name = "add",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
          .apply {
            path(
              fill = SolidColor(Color.Black),
              fillAlpha = 1f,
              stroke = null,
              strokeAlpha = 1f,
              strokeLineWidth = 1f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Bevel,
              strokeLineMiter = 1f,
              pathFillType = PathFillType.Companion.NonZero,
            ) {
              moveTo(11f, 13f)
              horizontalLineTo(5f)
              verticalLineTo(11f)
              horizontalLineToRelative(6f)
              verticalLineTo(5f)
              horizontalLineToRelative(2f)
              verticalLineToRelative(6f)
              horizontalLineToRelative(6f)
              verticalLineToRelative(2f)
              horizontalLineTo(13f)
              verticalLineToRelative(6f)
              horizontalLineTo(11f)
              verticalLineTo(13f)
              close()
            }
          }
          .build()
      return _add!!
    }

  private var _add: ImageVector? = null

  val check_circle: ImageVector
    get() {
      if (_check_circle != null) {
        return _check_circle!!
      }
      _check_circle =
        ImageVector.Builder(
          name = "check_circle",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
          .apply {
            path(
              fill = SolidColor(Color.Black),
              fillAlpha = 1f,
              stroke = null,
              strokeAlpha = 1f,
              strokeLineWidth = 1f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Bevel,
              strokeLineMiter = 1f,
              pathFillType = PathFillType.Companion.NonZero,
            ) {
              moveTo(10.6f, 16.6f)
              lineTo(17.65f, 9.55f)
              lineToRelative(-1.4f, -1.4f)
              lineTo(10.6f, 13.8f)
              lineTo(7.75f, 10.95f)
              lineToRelative(-1.4f, 1.4f)
              lineTo(10.6f, 16.6f)
              close()
              moveTo(12f, 22f)
              quadTo(9.93f, 22f, 8.1f, 21.21f)
              quadTo(6.28f, 20.43f, 4.93f, 19.08f)
              quadTo(3.58f, 17.73f, 2.79f, 15.9f)
              reflectiveQuadTo(2f, 12f)
              quadTo(2f, 9.92f, 2.79f, 8.1f)
              quadTo(3.58f, 6.27f, 4.93f, 4.93f)
              quadTo(6.28f, 3.57f, 8.1f, 2.79f)
              quadTo(9.93f, 2f, 12f, 2f)
              reflectiveQuadToRelative(3.9f, 0.79f)
              reflectiveQuadToRelative(3.17f, 2.14f)
              quadToRelative(1.35f, 1.35f, 2.14f, 3.17f)
              quadTo(22f, 9.92f, 22f, 12f)
              reflectiveQuadToRelative(-0.79f, 3.9f)
              reflectiveQuadToRelative(-2.14f, 3.17f)
              quadToRelative(-1.35f, 1.35f, -3.17f, 2.14f)
              reflectiveQuadTo(12f, 22f)
              close()
            }
          }
          .build()
      return _check_circle!!
    }

  private var _check_circle: ImageVector? = null

  @Suppress("CheckReturnValue")
  val close: ImageVector
    get() {
      if (_close != null) {
        return _close!!
      }
      _close =
        ImageVector.Builder(
          name = "close",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
          .apply {
            path(
              fill = SolidColor(Color.Black),
              fillAlpha = 1f,
              stroke = null,
              strokeAlpha = 1f,
              strokeLineWidth = 1f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Bevel,
              strokeLineMiter = 1f,
              pathFillType = PathFillType.Companion.NonZero,
            ) {
              moveTo(6.4f, 19f)
              lineTo(5f, 17.6f)
              lineTo(10.6f, 12f)
              lineTo(5f, 6.4f)
              lineTo(6.4f, 5f)
              lineTo(12f, 10.6f)
              lineTo(17.6f, 5f)
              lineTo(19f, 6.4f)
              lineTo(13.4f, 12f)
              lineTo(19f, 17.6f)
              lineTo(17.6f, 19f)
              lineTo(12f, 13.4f)
              lineTo(6.4f, 19f)
              close()
            }
          }
          .build()
      return _close!!
    }

  private var _close: ImageVector? = null

  @Suppress("CheckReturnValue")
  val lock: ImageVector
    get() {
      if (_lock != null) {
        return _lock!!
      }
      _lock =
        ImageVector.Builder(
          name = "lock",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
          .apply {
            path(
              fill = SolidColor(Color.Black),
              fillAlpha = 1f,
              stroke = null,
              strokeAlpha = 1f,
              strokeLineWidth = 1f,
              strokeLineCap = StrokeCap.Butt,
              strokeLineJoin = StrokeJoin.Bevel,
              strokeLineMiter = 1f,
              pathFillType = PathFillType.Companion.NonZero,
            ) {
              moveTo(6f, 22f)
              quadTo(5.18f, 22f, 4.59f, 21.41f)
              reflectiveQuadTo(4f, 20f)
              verticalLineTo(10f)
              quadTo(4f, 9.17f, 4.59f, 8.59f)
              reflectiveQuadTo(6f, 8f)
              horizontalLineTo(7f)
              verticalLineTo(6f)
              quadTo(7f, 3.92f, 8.46f, 2.46f)
              reflectiveQuadTo(12f, 1f)
              reflectiveQuadToRelative(3.54f, 1.46f)
              reflectiveQuadTo(17f, 6f)
              verticalLineTo(8f)
              horizontalLineToRelative(1f)
              quadToRelative(0.82f, 0f, 1.41f, 0.59f)
              reflectiveQuadTo(20f, 10f)
              verticalLineTo(20f)
              quadToRelative(0f, 0.82f, -0.59f, 1.41f)
              reflectiveQuadTo(18f, 22f)
              horizontalLineTo(6f)
              close()
              moveToRelative(7.41f, -5.59f)
              quadTo(14f, 15.83f, 14f, 15f)
              reflectiveQuadTo(13.41f, 13.59f)
              reflectiveQuadTo(12f, 13f)
              reflectiveQuadToRelative(-1.41f, 0.59f)
              quadTo(10f, 14.18f, 10f, 15f)
              reflectiveQuadToRelative(0.59f, 1.41f)
              reflectiveQuadTo(12f, 17f)
              reflectiveQuadToRelative(1.41f, -0.59f)
              close()
              moveTo(9f, 8f)
              horizontalLineToRelative(6f)
              verticalLineTo(6f)
              quadTo(15f, 4.75f, 14.13f, 3.88f)
              reflectiveQuadTo(12f, 3f)
              reflectiveQuadTo(9.88f, 3.88f)
              reflectiveQuadTo(9f, 6f)
              verticalLineTo(8f)
              close()
            }
          }
          .build()
      return _lock!!
    }

  private var _lock: ImageVector? = null

}
