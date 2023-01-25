package com.enicom.nims.schedule;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
	@Value("#{config['schedule.Smart']}")
	private String schedule_Smart;

	@Value("#{config['schedule.LoanReturn']}")
	private String schedule_LoanReturn;

	@Value("#{config['schedule.Return']}")
	private String schedule_Return;

	@Value("#{config['schedule.ResvLoan']}")
	private String schedule_ResvLoan;

	@Value("#{config['schedule.Gate']}")
	private String schedule_Gate;

	@Value("#{config['schedule.AntiLost']}")
	private String schedule_AntiLost;

	@Value("#{config['schedule.Common']}")
	private String schedule_Common;

	@Bean
	public Executor taskExecutor() {
		return Executors.newSingleThreadScheduledExecutor();
	}
	
	private CommonScheduler commonScheduler;
	private GateScheduler gateScheduler;
	private SLSScheduler slsScheduler;
	private LoanReturnScheduler loanReturnScheduler;
	private ResvLoanScheduler resvLoanScheduler;
	private ReturnScheduler returnScheduler;
	private AntiLostScheduler antilostScheduler;
	
	@Autowired
	public ScheduleConfig(CommonScheduler commonScheduler, GateScheduler gateScheduler, SLSScheduler slsScheduler, LoanReturnScheduler loanReturnScheduler, ResvLoanScheduler resvLoanScheduler, ReturnScheduler returnScheduler, AntiLostScheduler antilostScheduler) {
		this.commonScheduler = commonScheduler;
		this.gateScheduler = gateScheduler;
		this.slsScheduler = slsScheduler;
		this.loanReturnScheduler = loanReturnScheduler;
		this.resvLoanScheduler = resvLoanScheduler;
		this.returnScheduler = returnScheduler;
		this.antilostScheduler = antilostScheduler;
	}
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
		// API 최초 실행 시 동작
		try {
			if(schedule_Smart.equalsIgnoreCase("Y")) {
				slsScheduler.scheduleInit();
			}
			if(schedule_LoanReturn.equalsIgnoreCase("Y")) {
				loanReturnScheduler.scheduleInit();
			}
			if(schedule_Return.equalsIgnoreCase("Y")) {
				returnScheduler.scheduleInit();
			}
			if(schedule_ResvLoan.equalsIgnoreCase("Y")) {
				resvLoanScheduler.scheduleInit();
			}
			if(schedule_Gate.equalsIgnoreCase("Y")) {
				gateScheduler.scheduleInit();
			}
			if(schedule_AntiLost.equalsIgnoreCase("Y")) {
				antilostScheduler.scheduleInit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 매일 6시 동작
		taskRegistrar.addCronTask(new Runnable() {
			@Override
			public void run() {
				try {
					if(schedule_Smart.equalsIgnoreCase("Y")) {
						slsScheduler.scheduleDay();
					}
					if(schedule_LoanReturn.equalsIgnoreCase("Y")) {
						loanReturnScheduler.scheduleDay();
					}
					if(schedule_Return.equalsIgnoreCase("Y")) {
						returnScheduler.scheduleDay();
					}
					if(schedule_ResvLoan.equalsIgnoreCase("Y")) {
						resvLoanScheduler.scheduleDay();
					}
					if(schedule_Gate.equalsIgnoreCase("Y")) {
						gateScheduler.scheduleDay();
					}
					if(schedule_AntiLost.equalsIgnoreCase("Y")) {
						antilostScheduler.scheduleDay();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "0 0 06 * * *");

		// 매시간 동작
		taskRegistrar.addCronTask(new Runnable() {
			@Override
			public void run() {
				try {
					if(schedule_Smart.equalsIgnoreCase("Y")) {
						slsScheduler.scheduleHour();
					}
					if(schedule_LoanReturn.equalsIgnoreCase("Y")) {
						loanReturnScheduler.scheduleHour();
					}
					if(schedule_Return.equalsIgnoreCase("Y")) {
						returnScheduler.scheduleHour();
					}
					if(schedule_ResvLoan.equalsIgnoreCase("Y")) {
						resvLoanScheduler.scheduleHour();
					}
					if(schedule_Gate.equalsIgnoreCase("Y")) {
						gateScheduler.scheduleHour();
					}
					if(schedule_AntiLost.equalsIgnoreCase("Y")) {
						antilostScheduler.scheduleHour();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "0 0 * * * *");

		// 10분마다 동작
		taskRegistrar.addCronTask(new Runnable() {
			@Override
			public void run() {
				try {
					if(schedule_Smart.equalsIgnoreCase("Y")) {
						slsScheduler.scheduleMinuteTen();
					}
					if(schedule_LoanReturn.equalsIgnoreCase("Y")) {
						loanReturnScheduler.scheduleMinuteTen();
					}
					if(schedule_Return.equalsIgnoreCase("Y")) {
						returnScheduler.scheduleMinuteTen();
					}
					if(schedule_ResvLoan.equalsIgnoreCase("Y")) {
						resvLoanScheduler.scheduleMinuteTen();
					}
					if(schedule_Gate.equalsIgnoreCase("Y")) {
						gateScheduler.scheduleMinuteTen();
					}
					if(schedule_AntiLost.equalsIgnoreCase("Y")) {
						antilostScheduler.scheduleMinuteTen();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "0 */10 * * * *");

		// 매분 마다 동작
		taskRegistrar.addCronTask(new Runnable() {
			@Override
			public void run() {
				try {
					if(schedule_Smart.equalsIgnoreCase("Y")) {
						slsScheduler.scheduleMinuteOne();
					}
					if(schedule_LoanReturn.equalsIgnoreCase("Y")) {
						loanReturnScheduler.scheduleMinuteOne();
					}
					if(schedule_Return.equalsIgnoreCase("Y")) {
						returnScheduler.scheduleMinuteOne();
					}
					if(schedule_ResvLoan.equalsIgnoreCase("Y")) {
						resvLoanScheduler.scheduleMinuteOne();
					}
					if(schedule_Gate.equalsIgnoreCase("Y")) {
						gateScheduler.scheduleMinuteOne();
					}
					if(schedule_AntiLost.equalsIgnoreCase("Y")) {
						antilostScheduler.scheduleMinuteOne();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "0 * * * * *");

		// 매10초 마다 동작
		taskRegistrar.addCronTask(new Runnable() {
			@Override
			public void run() {
				try {
					if(schedule_Common.equalsIgnoreCase("Y")) {
						commonScheduler.scheduleSecondTen();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "0/10 * * * * *");
	}
}
