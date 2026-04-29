package com.example.real_time_messaging_system.dto;

import com.example.real_time_messaging_system.entity.OnlineStatus;

public record PresenceUpdate(
        String email,
        OnlineStatus onlineStatus
) {
}
