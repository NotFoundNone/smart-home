package dev.project.api.controller;

import dev.project.api.dto.DeviceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "State")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная обработка запроса"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Состояние не найдено"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public interface StateApi {

    String PREFIX = "/api/state";

    @PostMapping(PREFIX + "/change")
    @Operation(summary = "Изменение состояния устройства")
    public ResponseEntity<EntityModel<DeviceDto>> changeDeviceState(@RequestParam Long deviceId, @RequestParam Long stateTypeId);

    @PostMapping(PREFIX + "/toggle")
    @Operation(summary = "Включение-выключение устройства")
    public ResponseEntity<EntityModel<DeviceDto>> toggleDevice(@RequestParam Long deviceId);

    @PostMapping(PREFIX + "/temperature")
    @Operation(summary = "Отправка температуры")
    public ResponseEntity<EntityModel<DeviceDto>> sendDeviceTemperature(@RequestParam Long deviceId, @RequestParam double temperature);

}
