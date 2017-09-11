package com.xingyanping.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xingyanping.datamodel.ClientPortRelationship;

public class ClientGroupValidationHelper {
	private Map<String, List<ClientPortRelationship>> clientGroupMap;
	public ClientGroupValidationHelper(List<ClientPortRelationship> clientGroup) {
		this.clientGroupMap = new HashMap<>();
		for (ClientPortRelationship client : clientGroup) {
			List<ClientPortRelationship> list = this.clientGroupMap.get(client.getPort());
			if (list == null) {
				list = new ArrayList<>();
				this.clientGroupMap.put(client.getPort(), list);
			}
			list.add(client);
		}
	}

	public ValidationError validate() {
		for (String port : this.clientGroupMap.keySet()) {
			List<ClientPortRelationship> group = this.clientGroupMap.get(port);
			ValidationError error = validateGroup(group);
			if (!error.result) {
				return error;
			}
		}
		return new ValidationError(true);
	}

	private ValidationError validateGroup(List<ClientPortRelationship> group) {
		Collections.sort(group, (ClientPortRelationship o1, ClientPortRelationship o2) -> {
			if (o1.getExpiringDate() == null && o2.getExpiringDate() == null) {
				return o1.getEffectiveDate().compareTo(o2.getEffectiveDate());
			} else if (o1.getExpiringDate() == null) {
				return 1;
			} else if (o2.getExpiringDate() == null) {
				return -1;
			} else {
				return o1.getEffectiveDate().compareTo(o2.getEffectiveDate());
			}
		});
		Date previousEffectiveDate = null;
		Date previousExpiringDate = null;
		for (ClientPortRelationship client : group) {
			if (previousEffectiveDate == null) {
				previousEffectiveDate = client.getEffectiveDate();
				previousExpiringDate = client.getExpiringDate();
				if (previousExpiringDate != null && previousExpiringDate.before(previousEffectiveDate)) {
					return new ValidationError("端口号 " + client.getPort() + " 的客户关系组中有 结束时间早于起始时间的");
				}
				continue;
			}
			if (previousExpiringDate == null) {
				return new ValidationError("端口号 " + client.getPort() + " 的客户关系组中有 未结束又开始的");
			}
			if (previousExpiringDate.after(client.getEffectiveDate())) {
				return new ValidationError("端口号 " + client.getPort() + " 的客户关系组中有 未结束又开始的");
			}
			previousEffectiveDate = client.getEffectiveDate();
			previousExpiringDate = client.getExpiringDate();
		}
		return new ValidationError(true);
	}

}
