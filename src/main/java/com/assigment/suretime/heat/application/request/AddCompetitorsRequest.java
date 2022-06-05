package com.assigment.suretime.heat.application.request;

import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddCompetitorsRequest {
    List<String> competitorsUUIDs;
}
