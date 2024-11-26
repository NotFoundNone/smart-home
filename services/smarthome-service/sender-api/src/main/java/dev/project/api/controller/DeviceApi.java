package dev.project.api.controller;

import dev.project.api.dto.DeviceDto;
import dev.project.api.dto.DeviceResourceDtoRequest;
import dev.project.api.dto.GetAllDevicesResponse;
import dev.project.api.dto.RequestDto;
import dev.project.api.exception.DeviceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Device")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная обработка запроса"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Устройство не найдено"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public interface DeviceApi {

    String PREFIX = "/api/device";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "Создание нового устройства")
    public ResponseEntity<DeviceResourceDtoRequest> addDevice(@RequestBody DeviceDto device);

    @GetMapping(PREFIX + "/{deviceId}")
    @Operation(summary = "Получение информации об устройстве")
    public ResponseEntity<DeviceResourceDtoRequest> getDevice(@PathVariable Long deviceId) throws DeviceNotFoundException;

    @GetMapping(PREFIX+ "/")
    @Operation(summary = "Получение информации о всех устройствах")
    public ResponseEntity<GetAllDevicesResponse> getAllDevices();

    @PatchMapping(PREFIX + "/update")
    @Operation(summary = "Обновление информации об устройстве")
    public ResponseEntity<DeviceResourceDtoRequest> updateDevice(@RequestBody DeviceDto device);

    @DeleteMapping(PREFIX + "/delete/{deviceId}")
    @Operation(summary = "Удаление устройства")
    public ResponseEntity<RequestDto> deleteDevice(@PathVariable Long deviceId);
}
