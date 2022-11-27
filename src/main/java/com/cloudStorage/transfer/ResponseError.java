package com.cloudStorage.transfer;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError {
    private String message;
    private int id;
}
