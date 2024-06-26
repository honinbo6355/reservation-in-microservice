package com.example.hhplus.reservation.api;

import com.example.hhplus.reservation.api.concert.ConcertController;
import com.example.hhplus.reservation.api.concert.dto.ConcertDateResponse;
import com.example.hhplus.reservation.api.concert.dto.ConcertDetailDto;
import com.example.hhplus.reservation.domain.concert.ConcertService;
import com.example.hhplus.reservation.domain.user.ReservationToken;
import com.example.hhplus.reservation.domain.user.ReservationTokenRepository;
import com.example.hhplus.reservation.domain.user.ReservationTokenStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConcertController.class)
public class ConcertControllerTest {

    @MockBean
    private ConcertService concertService;

    @MockBean
    private ReservationTokenRepository reservationTokenRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("예약_가능_날짜_조회_성공")
    public void 예약_가능_날짜_조회_성공() throws Exception {
        // given
        String token = "97ef7717-2bcd-4e23-9e7a-3d9bfa4ea5dd";
        Long concertId = 1L;
        String concertName = "아이유 콘서트";
        List<ConcertDetailDto> concertInfos = new ArrayList<>();
        concertInfos.add(new ConcertDetailDto(1L, LocalDateTime.of(2024, 2, 15, 13, 0)));
        concertInfos.add(new ConcertDetailDto(1L, LocalDateTime.of(2024, 2, 20, 15, 0)));
        concertInfos.add(new ConcertDetailDto(1L, LocalDateTime.of(2024, 3, 15, 13, 0)));
        concertInfos.add(new ConcertDetailDto(1L, LocalDateTime.of(2024, 3, 20, 15, 0)));

        ConcertDateResponse concertDateResponse = new ConcertDateResponse(concertName, concertInfos);

        // when
        when(reservationTokenRepository.findByToken(token)).thenReturn(Optional.ofNullable(new ReservationToken(token, anyLong(), LocalDateTime.now())));
        when(concertService.getConcertDate(concertId)).thenReturn(concertDateResponse);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/concert/" + concertId + "/date")
                        .header("token", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.concertName").value(concertName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.concertInfos.size()").value(concertInfos.size()));
    }

    @Test
    @DisplayName("예약_가능_좌석_조회_성공")
    public void 예약_가능_좌석_조회_성공() throws Exception {
        // given
        String token = "97ef7717-2bcd-4e23-9e7a-3d9bfa4ea5dd";
        Long concertDetailId = 1L;
        List<Long> seatIds = List.of(1L, 2L, 3L, 4L);

        // when
        when(reservationTokenRepository.findByToken(token)).thenReturn(Optional.ofNullable(new ReservationToken(token, anyLong(), LocalDateTime.now())));
        when(concertService.getConcertSeat(concertDetailId)).thenReturn(seatIds);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/concert/" + concertDetailId + "/seat")
                        .header("token", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.size()").value(seatIds.size()));
    }
}
