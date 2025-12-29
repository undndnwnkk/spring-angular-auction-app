package com.auction.catalogservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(@NotBlank String name) { }
