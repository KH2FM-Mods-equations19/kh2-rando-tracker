package com.kh2rando.tracker.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

/**
 * Represents a single value as a [StateFlow].
 *
 * Useful for desiring a flow that emits a single item but never completes.
 */
fun <T> stateFlowOf(value: T): StateFlow<T> = MutableStateFlow(value).asStateFlow()

/**
 * Combines the most recently emitted values from 9 flows.
 */
@Suppress("UNCHECKED_CAST")
inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combineMany(
  flow1: Flow<T1>,
  flow2: Flow<T2>,
  flow3: Flow<T3>,
  flow4: Flow<T4>,
  flow5: Flow<T5>,
  flow6: Flow<T6>,
  flow7: Flow<T7>,
  flow8: Flow<T8>,
  flow9: Flow<T9>,
  crossinline transform: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R,
): Flow<R> {
  return combine(flow1, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9) { array ->
    transform(
      array[0] as T1,
      array[1] as T2,
      array[2] as T3,
      array[3] as T4,
      array[4] as T5,
      array[5] as T6,
      array[6] as T7,
      array[7] as T8,
      array[8] as T9,
    )
  }
}
