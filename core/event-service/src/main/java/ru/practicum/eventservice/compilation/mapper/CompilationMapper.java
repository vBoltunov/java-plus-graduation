package ru.practicum.eventservice.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.eventservice.compilation.model.Compilation;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoRequest;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoResponse;
import ru.practicum.interaction.dto.event.compilation.CompilationDtoUpdate;
import ru.practicum.eventservice.events.mapper.EventMapper;
import ru.practicum.eventservice.events.model.Event;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompilationMapper {

    CompilationDtoResponse toCompilationDto(Compilation compilation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "compilationDto.title")
    @Mapping(target = "pinned", source = "compilationDto.pinned")
    @Mapping(target = "events", source = "events")
    Compilation toUpdateCompilation(
            @MappingTarget Compilation compilation,
            CompilationDtoUpdate compilationDto,
            List<Event> events
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "events")
    Compilation toCompilation(CompilationDtoRequest compilationDto, List<Event> events);
}