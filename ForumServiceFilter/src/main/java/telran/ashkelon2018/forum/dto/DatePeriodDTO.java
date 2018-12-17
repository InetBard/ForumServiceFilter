package telran.ashkelon2018.forum.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class DatePeriodDTO {
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDateTime from;
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDateTime to;
}
