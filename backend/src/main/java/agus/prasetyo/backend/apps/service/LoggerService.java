package agus.prasetyo.backend.apps.service;

import agus.prasetyo.backend.apps.model.db.Logger;
import agus.prasetyo.backend.apps.model.dto.ArticleDTO;
import agus.prasetyo.backend.apps.model.dto.LoggerDTO;
import agus.prasetyo.backend.apps.model.dto.RoleDTO;
import agus.prasetyo.backend.apps.model.dto.UserDTO;
import agus.prasetyo.backend.apps.model.request.LoggerRequest;
import agus.prasetyo.backend.apps.repository.LoggerRepository;
import agus.prasetyo.backend.system.utils.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LoggerService {

    private final LoggerRepository loggerRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public LoggerService(LoggerRepository loggerRepository, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.loggerRepository = loggerRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public List<LoggerDTO> findAll() {
        return loggerRepository.findAll().stream()
                .map(logger -> {
                    LoggerDTO loggerDTO = modelMapper.map(logger, LoggerDTO.class);
                    loggerDTO.setRequestObj(parseJson(fixTrailingComma(logger.getRequest())));
                    loggerDTO.setResponseObj(parseJson(fixTrailingComma(logger.getResponse())));
                    return loggerDTO;
                })
                .collect(Collectors.toList());
    }

    @Cacheable(value = "all_logs")
    public List<LoggerDTO> findAll(String sortBy, String sortDirection) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDirection.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        // Fetch data dari repository dengan sorting
        return loggerRepository.findAll(sort).stream()
                .map(el -> modelMapper.map(el, LoggerDTO.class))
                .collect(Collectors.toList());
    }


    // @Cacheable(value = "detail_log", key = "#id")
    public LoggerDTO findByIdOrThrow(UUID id) throws ResourceNotFoundException {
        return loggerRepository.findById(id).map(logger -> {
                    LoggerDTO loggerDTO = modelMapper.map(logger, LoggerDTO.class);
                    loggerDTO.setRequestObj(parseJson(fixTrailingComma(logger.getRequest())));
                    loggerDTO.setResponseObj(parseJson(fixTrailingComma(logger.getResponse())));
                    return loggerDTO;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Logger not found with ID: " + id));
    }


    public LoggerDTO create(LoggerRequest LoggerRequest) {
        Logger logger = modelMapper.map(LoggerRequest, Logger.class);
        Logger savedLogger = loggerRepository.save(logger);
        return modelMapper.map(savedLogger, LoggerDTO.class);
    }

    public void deleteAll() {
        loggerRepository.deleteAll();
    }

    private Object parseJson(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON string: " + jsonString, e);
        }
    }

    public String fixTrailingComma(String json) {
        if (json != null) {
            return json.replaceAll(",\\s*([}\\]])", "$1"); // Menghapus koma sebelum } atau ]
        }
        return "{}";
    }
}
