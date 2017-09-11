package com.xingyanping.util;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.xingyanping.datamodel.ClientPortRelationship;

public class MatchClientGetEffectiveCprsListTest {
	@Test(expected = Exception.class)
	public void cprsWithNullEffectiveDateShouldThrowException() throws Exception {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		ClientPortRelationship cprs = new ClientPortRelationship();
		cprs.setEffectiveDate(null);
		cprsList.add(cprs);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date reportDate = df.parse("2017-09-08");
		MatchClientUtil.getEffectiveCprsList(cprsList, reportDate);
	}
	@Test
	public void cprsWithFutureEffectiveDateShouldBeRemoved() throws Exception {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		ClientPortRelationship cprs = new ClientPortRelationship();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cprs.setEffectiveDate(df.parse("2017-09-09"));
		cprsList.add(cprs);
		Date reportDate = df.parse("2017-09-08");
		List<ClientPortRelationship> effectiveCprsList = MatchClientUtil.getEffectiveCprsList(cprsList, reportDate);
		assertThat(effectiveCprsList, empty());
	}
	@Test
	public void cprsWithPastExpiringDateShouldBeRemoved() throws Exception {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		ClientPortRelationship cprs = new ClientPortRelationship();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cprs.setEffectiveDate(df.parse("2017-09-06"));
		cprs.setExpiringDate(df.parse("2017-09-08"));
		cprsList.add(cprs);
		Date reportDate = df.parse("2017-09-09");
		List<ClientPortRelationship> effectiveCprsList = MatchClientUtil.getEffectiveCprsList(cprsList, reportDate);
		assertThat(effectiveCprsList, empty());
	}
	@Test
	public void cprsWithNullExpiringDateShouldBeKept() throws Exception {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		ClientPortRelationship cprs = new ClientPortRelationship();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cprs.setEffectiveDate(df.parse("2017-09-06"));
		cprs.setExpiringDate(null);
		cprsList.add(cprs);
		Date reportDate = df.parse("2017-09-09");
		List<ClientPortRelationship> effectiveCprsList = MatchClientUtil.getEffectiveCprsList(cprsList, reportDate);
		assertThat(effectiveCprsList, hasItem(cprs));
	}
	@Test
	public void EffectiveCprsShouldBeKept() throws Exception {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		ClientPortRelationship cprs = new ClientPortRelationship();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cprs.setEffectiveDate(df.parse("2017-09-06"));
		cprs.setExpiringDate(df.parse("2017-09-10"));
		cprsList.add(cprs);
		Date reportDate = df.parse("2017-09-09");
		List<ClientPortRelationship> effectiveCprsList = MatchClientUtil.getEffectiveCprsList(cprsList, reportDate);
		assertThat(effectiveCprsList, hasItem(cprs));
	}
	@Test
	public void cprsHasSameEffectiveDateShouldBeKept() throws Exception {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		ClientPortRelationship cprs = new ClientPortRelationship();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cprs.setEffectiveDate(df.parse("2017-09-06"));
		cprs.setExpiringDate(df.parse("2017-09-10"));
		cprsList.add(cprs);
		Date reportDate = df.parse("2017-09-06");
		List<ClientPortRelationship> effectiveCprsList = MatchClientUtil.getEffectiveCprsList(cprsList, reportDate);
		assertThat(effectiveCprsList, hasItem(cprs));
	}
	@Test
	public void cprsHasSameExpiringDateShouldBeKept() throws Exception {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		ClientPortRelationship cprs = new ClientPortRelationship();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		cprs.setEffectiveDate(df.parse("2017-09-06"));
		cprs.setExpiringDate(df.parse("2017-09-10"));
		cprsList.add(cprs);
		Date reportDate = df.parse("2017-09-10");
		List<ClientPortRelationship> effectiveCprsList = MatchClientUtil.getEffectiveCprsList(cprsList, reportDate);
		assertThat(effectiveCprsList, hasItem(cprs));
	}
}
