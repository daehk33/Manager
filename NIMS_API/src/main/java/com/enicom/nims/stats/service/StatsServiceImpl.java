package com.enicom.nims.stats.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service(value = "statsService")
public class StatsServiceImpl implements StatsService {
	private ServiceUtil serviceUtil;

	@Autowired
	public StatsServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> getStatsList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "stats.getStatsAllListbyUserCnt", "stats.getStatsAllListbyUser" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "stats.getStatsAllListByDateCnt", "stats.getStatsAllListByDate" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "stats.getStatsAllListByTimeCnt", "stats.getStatsAllListByTime" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsDevice(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsAllListByDeviceCnt", "stats.getStatsAllListByDevice" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsCalendar(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsAllListByCalendarCnt", "stats.getStatsAllListbyCalendar" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsSLSReservationLoanList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsSLSReservationLoanListCnt", "stats.getStatsSLSReservationLoanList" };
		String[] columns = { "library_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String ,Object> getStatsUnmannedReturnList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsUnmannedReturnListCnt", "stats.getStatsUnmannedReturnList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String ,Object> getStatsUnmannedReturnDate(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "getStatsUnmannedReturnDateCnt", "getStatsUnmannedReturnDate" };
		String[] columns = { "library_key", "device_key" };
		
		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String ,Object> getStatsUnmannedReturnTime(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "getStatsUnmannedReturnTimeCnt", "getStatsUnmannedReturnTime" };
		String[] columns = { "library_key", "device_key" };
		
		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String ,Object> getStatsUnmannedReturnDevice(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "getStatsUnmannedReturnDeviceCnt", "getStatsUnmannedReturnDevice" };
		String[] columns = { "library_key", "device_key" };
		
		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String ,Object> getStatsUnmannedReturnCalendar(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "getStatsUnmannedReturnCalendarCnt", "getStatsUnmannedReturnCalendar" };
		String[] columns = { "library_key", "device_key" };
		
		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsLoanReturnList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsLoanReturnAllListCnt", "stats.getStatsLoanReturnAllList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	public Map<String, Object> getStatsLoanReturnDate(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsLoanReturnAllListbyDateCnt", "stats.getStatsLoanReturnAllListbyDate" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	public Map<String, Object> getStatsLoanReturnTime(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsLoanReturnAllListbyTimeCnt", "stats.getStatsLoanReturnAllListbyTime" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsLoanReturnDevice(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsLoanReturnAllListbyDeviceCnt", "stats.getStatsLoanReturnAllListbyDevice" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	public Map<String, Object> getStatsLoanReturnCalendar(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsLoanReturnAllListbyCalendarCnt",
				"stats.getStatsLoanReturnAllListbyCalendar" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	public Map<String, Object> getStatsReturnList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = new String[] { "stats.getStatsReturnListCnt", "stats.getStatsReturnList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	public Map<String, Object> getStatsReturnDate(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = new String[] { "stats.getStatsReturnListByDateCnt", "stats.getStatsReturnListByDate" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsReturnTime(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsReturnListByTimeCnt", "stats.getStatsReturnListByTime" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsReturnDevice(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsReturnListByDeviceCnt", "stats.getStatsReturnListByDevice" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsReturnCalendar(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsReturnCalendarCnt", "stats.getStatsReturnCalendar" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsResvLoanList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsResvLoanListCnt", "stats.getStatsResvLoanList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsResvLoanTime(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsResvLoanTimeCnt", "stats.getStatsResvLoanTime" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsResvLoanDate(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsResvLoanDateCnt", "stats.getStatsResvLoanDate" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsResvLoanDevice(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsResvLoanDeviceCnt", "stats.getStatsResvLoanDevice" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsResvLoanCalendar(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsResvLoanCalendarCnt", "stats.getStatsResvLoanCalendar" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsGateList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsGateListCnt", "stats.getStatsGateList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsGateTime(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsGateTimeCnt", "stats.getStatsGateTime" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsGateDate(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsGateDateCnt", "stats.getStatsGateDate" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsGateDevice(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsGateDeviceCnt", "stats.getStatsGateDevice" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsGateCalendar(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsGateCalendarCnt", "stats.getStatsGateCalendar" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsAntiLostList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsAntiLostListCnt", "stats.getStatsAntiLostList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsAntiLostTime(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsTimeCnt", "stats.getStatsAntiLostTime" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsAntiLostDate(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsDateCnt", "stats.getStatsAntiLostDate" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String, Object> getStatsAntiLostDay(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "stats.getStatsDayCnt", "stats.getStatsAntiLostDay" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String, Object> getStatsAntiLostDevice(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsAntiLostDeviceCnt", "stats.getStatsAntiLostDevice" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getStatsAntiLostCalendar(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "stats.getStatsAntiLostCalendarCnt", "stats.getStatsAntiLostCalendar" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
}
