package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.assistant.EventType;
import ru.yandex.practicum.filmorate.assistant.Operation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

/**
 * Класс-модель для создания объекта события со свойствами <>timestamp</>, <>userId</>, <>eventType</>,
 * <>operation</>, <>eventId</>, <>entityId</>
 */
@Data
@Builder(toBuilder = true)
public class Feed {
	/**
	 * Поле времени события.
	 */
	@PastOrPresent(message = "Время события не может быть в будущем.")
	@NotNull(message = "Время события не может отсутствовать.")
	private Long timestamp;
	/**
	 * Поле-идентификатора пользователя повлиявшим на событие
	 */
	@NotNull(message = "Идентификатор пользователя не может отсутствовать.")
	private Long userId;
	/**
	 * Поле-тип события.
	 */
	@NotBlank(message = "Тип события не может отсутствовать.")
	private EventType eventType;
	/**
	 * Поле-тип произведенной операции.
	 */
	@NotBlank(message = "Операция события не может отсутствовать.")
	private Operation operation;
	/**
	 * Поле-идентификатор события.
	 */
	@NotNull(message = "Идентификатор события не может отсутствовать.")
	private Long eventId;
	/**
	 * Поле-идентификатор сущности с которой связано событие.
	 */
	@NotNull(message = "Идентификатор сущности с которой связано событие не может отсутствовать.")
	private Long entityId;
}
