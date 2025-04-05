package com.example.backend.dto.generation;

import com.example.backend.dto.MonsterDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonsterWithCountDto {
    private MonsterDto monster;
    private Integer count;
}
