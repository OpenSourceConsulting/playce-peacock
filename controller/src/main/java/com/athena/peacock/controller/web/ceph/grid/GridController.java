/* 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * THLee			2015. 10. 15.		First Draft.
 */
package com.athena.peacock.controller.web.ceph.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.ceph.CephBaseController;
import com.athena.peacock.controller.web.ceph.CephService;
import com.athena.peacock.controller.web.ceph.base.CephDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.SimpleJsonResponse;

/**
 * <pre>
 * 
 * </pre>
 * @author THLee
 * @version 1.0
 */
@Controller
@RequestMapping("/ceph/grid")
public class GridController extends CephBaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GridController.class);
	
	@Inject
	@Named("cephService")
	private CephService cephService;

	/**
	 * <pre>
	 * Server List. HOST Grid와 Main Grid에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/hostlist")
	public @ResponseBody SimpleJsonResponse getHostList(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = calamariSubmit("/server", HttpMethod.GET);
			JsonNode hosts = (JsonNode) response;

			ArrayNode rootNode = createArrayNode();
			ObjectNode subNode = null;
			
			JsonNode serviceList = null;
			String hostName = null;
			if (hosts.isArray()) {
				ArrayList<String> idOsd = new ArrayList<String>();
				ArrayList<String> idMon = new ArrayList<String>();
				
				for (JsonNode host : hosts) {
					hostName = host.path("hostname").asText();

					serviceList = (JsonNode)host.path("services");
					if (serviceList.isArray()) {
						idOsd.clear();
						idMon.clear();
						Boolean isRunnong = true;
						for (JsonNode service : serviceList){
							String sType = service.path("type").asText();
							String sId = service.path("id").asText();

							if (sType.equals("osd")) {
								idOsd.add(sId);
							} else if (sType.equals("mon")) {
								idMon.add(sId);
							}

							if (!service.path("running").asBoolean()) {
								isRunnong = service.path("running").asBoolean();
							}
						}
						
						Collections.sort(idOsd);
						Collections.sort(idMon);

						String strType = "";
						String strId = "";
						String sTemp;

						if (idOsd.size() > 0) {
							sTemp = "";
							for (String value : idOsd) {
								if (!sTemp.isEmpty()) sTemp += ", ";
								sTemp += value;
							}
							strId += "OSD: " + sTemp;
							strType += "OSD(" + idOsd.size() + ")";
						}

						if (idMon.size() > 0) {
							if (!strId.isEmpty()) strId += " / ";
							if (!strType.isEmpty()) strType += " / ";

							sTemp = "";
							for (String value : idMon) {
								if (!sTemp.isEmpty()) sTemp += ", ";
								sTemp += value;
							}
							strId += "MON: " + sTemp;
							strType += "MON(" + idMon.size() + ")";
						}
						
						subNode = createObjectNode();
						subNode.put("hostname", hostName);
						subNode.put("type", strType );
						subNode.put("id", strId);
						subNode.put("running", isRunnong);
						rootNode.add(subNode);
					}
				}
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("server list가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("server list 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * ceph status 명령 결과를 얻는다. Main Detail에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cephstatus")
	public @ResponseBody SimpleJsonResponse getCephStatus(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = execute("ceph status");
			
			jsonRes.setSuccess(true);
			jsonRes.setData(response);
			jsonRes.setMsg("ceph status가 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("ceph status 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v2/cluster/{fsid}/mon addr필드를 분리. MON Grid에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/monlist")
	public @ResponseBody SimpleJsonResponse getMonList(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = calamariSubmit("/cluster", HttpMethod.GET);
			JsonNode clusters = (JsonNode) response;

			ArrayNode rootNode = createArrayNode();
			ObjectNode subNode = null;
			
			JsonNode monList = null;
			String clusterId = null;
			if (clusters.isArray()) {
				for (JsonNode cluster : clusters) {
					clusterId = cluster.path("id").asText();
					monList = (JsonNode) calamariSubmit("/cluster/" + clusterId + "/mon", HttpMethod.GET);
					if (monList.isArray()) {
						for (JsonNode mon : monList){
							String addr = mon.path("addr").asText();
							String ip = addr.substring(0, addr.indexOf(":"));
							String port = addr.substring(addr.indexOf(":")+1, addr.indexOf("/"));
							String pid = addr.substring(addr.indexOf("/")+1);

							subNode = createObjectNode();
							subNode.put("name", mon.path("name").asText());
							subNode.put("rank", mon.path("rank").asInt());
							subNode.put("in_quorum", mon.path("in_quorum").asBoolean());
							subNode.put("server", mon.path("server").asText());
							subNode.put("addr", addr);
							subNode.put("ip", ip);
							subNode.put("port", port);
							subNode.put("pid", pid);

							rootNode.add(subNode);
						}
					}
				}
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("mon list이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("mon list 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v0.1/health, health_services.mons. MON Detail에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mondetail")
	public @ResponseBody SimpleJsonResponse getMonDetail(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/health", HttpMethod.GET);
			JsonNode outputs = (JsonNode) response;
			JsonNode healths = outputs.path("output").path("health").path("health_services");

			ArrayNode rootNode = createArrayNode();
			JsonNode monList = null;
			if (healths.isArray()) {
				for (JsonNode health : healths) {
					monList = (JsonNode)health.path("mons");
					if (monList.isArray()) {
						for (JsonNode mon : monList) {
							rootNode.add(mon);
						}
					}
				}
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("mon detail이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("mon detail 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v0.1/osd/tree & /api/v0.1/osd/dump 를 조합하여 list 생성. OSD Grid에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/osdlist")
	public @ResponseBody SimpleJsonResponse getOsdList(SimpleJsonResponse jsonRes) throws Exception {
		try {
			List<String> alNames = new ArrayList<String>();
			List<String> alHosts = new ArrayList<String>();
			List<Integer> alIds = new ArrayList<Integer>();

			Object response = managementSubmit("/osd/tree", HttpMethod.GET);
			JsonNode apiRes = (JsonNode) response;
			JsonNode osdDump = apiRes.path("output").path("nodes");

			if (osdDump.isArray()) {
				for (JsonNode node : osdDump){
					String nType = node.path("type").asText().trim();
					if (nType.compareTo("osd") == 0) {
						alNames.add(node.path("name").asText());
						alHosts.add("");
						alIds.add(node.path("id").asInt());
					}
				}
				for (JsonNode node : osdDump){
					String nType = node.path("type").asText().trim();
					if (nType.compareTo("host") == 0) {
						String hostname = node.path("name").asText();
						JsonNode children = node.get("children");
						for (JsonNode child : children) {
							int id = child.asInt();
							int pos = alIds.indexOf(id);
							
							if (pos >= 0) alHosts.set(pos, hostname);
						}
					} 
				}
			}
			
			response = managementSubmit("/osd/dump", HttpMethod.GET);
			apiRes = (JsonNode) response;
			osdDump = apiRes.path("output").path("osds");

			ObjectNode subNode = null;

			ArrayNode osds = createArrayNode();
			if (osdDump.isArray()) {
				for (JsonNode osd : osdDump){
					int id = osd.path("osd").asInt();
					String name = "osd-" + id;
					String host = "";
					
					int pos = alIds.indexOf(id);
					if (pos >= 0) {
						name = alNames.get(pos);
						host = alHosts.get(pos);
					}
							
					String status = "";
					if (osd.path("up").asInt() == 1) status = "up";
					if (osd.path("in").asInt() == 1) {
						if (status != "") status += "/";
						status += "in";
					}
					
					String addr = osd.path("public_addr").asText();
					String ip = addr.substring(0, addr.indexOf(":"));
					String port = addr.substring(addr.indexOf(":")+1, addr.indexOf("/"));
					String pid = addr.substring(addr.indexOf("/")+1);

					subNode = createObjectNode();
					subNode.put("name", name);
					subNode.put("id", id);
					subNode.put("status", status);
					subNode.put("ip", ip);
					subNode.put("port", port);
					subNode.put("pid", pid);
					subNode.put("public_addr", addr);
					subNode.put("cluster_addr", osd.path("cluster_addr").asText());
					subNode.put("heartbeat_back_addr", osd.path("heartbeat_back_addr").asText());
					subNode.put("heartbeat_front_addr", osd.path("heartbeat_front_addr").asText());
					subNode.put("up_from", osd.path("up_from").asInt());
					subNode.put("host", host);
						  
					osds.add(subNode);
				}
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(osds);
			jsonRes.setMsg("osd list이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("osd list 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v2/cluster/{fsid}/pool . POOL Grid에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/poollist")
	public @ResponseBody SimpleJsonResponse getPoolList(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = calamariSubmit("/cluster", HttpMethod.GET);
			JsonNode clusters = (JsonNode) response;

			ArrayNode rootNode = createArrayNode();
			
			JsonNode poolList = null;
			String clusterId = null;
			if (clusters.isArray()) {
				for (JsonNode cluster : clusters) {
					clusterId = cluster.path("id").asText();
					poolList = (JsonNode) calamariSubmit("/cluster/" + clusterId + "/pool", HttpMethod.GET);
					if (poolList.isArray()) {
						for (JsonNode data : poolList) {
							rootNode.add(data);
						}
					}
				}
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("pool list이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("pool list 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v0.1/pg/dump_pools_json . POOL Detail에서 사용(실제는 poolid 넘기는 것 사용)
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pooldetail")
	public @ResponseBody SimpleJsonResponse getPoolDetailAll(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/pg/dump_pools_json", HttpMethod.GET);
			JsonNode outputs = (JsonNode) response;
			JsonNode apiRes = outputs.path("output");

			ArrayNode rootNode = createArrayNode();
			if (apiRes.isArray()) {
				rootNode.add(apiRes);
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("pool detail이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("pool detail 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}
	
	/**
	 * <pre>
	 * /api/v0.1/pg/dump_pools_json . POOL Detail에서 사용 (poolid에 해당하는 결과만 return)
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pooldetail/{poolid}")
	public @ResponseBody SimpleJsonResponse getPoolDetail(SimpleJsonResponse jsonRes, @PathVariable("poolid") Integer poolid) throws Exception {
		try {
			Object response = managementSubmit("/pg/dump_pools_json", HttpMethod.GET);
			JsonNode outputs = (JsonNode) response;
			JsonNode apiRes = outputs.path("output");

			ArrayNode rootNode = createArrayNode();
			if (apiRes.isArray()) {
				for(JsonNode data : apiRes) {
					if (data.path("poolid").asInt() == poolid) {
						rootNode.add(data);
						break;
					}
				}
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("pool detail/" + poolid + "이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("pool detail/" + poolid + " 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v0.1/pg/stat . PG Stat Grid에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pglist")
	public @ResponseBody SimpleJsonResponse getPgList(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/pg/stat", HttpMethod.GET);
			JsonNode outputs = (JsonNode) response;
			JsonNode apiRes = outputs.path("output");

			String version = apiRes.path("version").asText();
			Long bytesUsed = apiRes.path("raw_bytes_used").asLong();
			Long bytesAvail = apiRes.path("raw_bytes_avail").asLong();
			
			ArrayNode rootNode = createArrayNode();
			ObjectNode subNode = null;

			apiRes = outputs.path("output").path("num_pg_by_state");
			if (apiRes.isArray()) {
				for (JsonNode data : apiRes){
					subNode = createObjectNode();
					subNode.put("num", data.path("num").asInt());
					subNode.put("name", data.path("name").asText());
					subNode.put("version", version);
					subNode.put("raw_bytes_used", bytesUsed);
					subNode.put("raw_bytes_avail", bytesAvail);

					rootNode.add(subNode);
				}
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("pg list이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("pg list 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v0.1/pg/dump_json 의 pg_stats_sum. PG STAT Detail에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pgdetail")
	public @ResponseBody SimpleJsonResponse getPgDetail(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/pg/dump_json", HttpMethod.GET);
			JsonNode outputs = (JsonNode) response;
			JsonNode apiRes = outputs.path("output").path("pg_stats_sum");

			ArrayNode rootNode = createArrayNode();
			rootNode.add(apiRes);
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("pg detail이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("pg detail 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v0.1/osd/df -> output.nodes. Usage Grid에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/usagelist")
	public @ResponseBody SimpleJsonResponse getUsageList(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/df", HttpMethod.GET);
			JsonNode outputs = (JsonNode) response;
			JsonNode apiRes = outputs.path("output").path("nodes");
			
			ArrayNode rootNode = createArrayNode();
			if (apiRes.isArray()) {
				for (JsonNode data : apiRes){
					rootNode.add(data);
				}
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("usage list이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("usage list 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v0.1/osd/df -> output.summary. Usage Detail에서 사용
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/usagedetail")
	public @ResponseBody SimpleJsonResponse getUsageDetail(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = managementSubmit("/osd/df", HttpMethod.GET);
			JsonNode outputs = (JsonNode) response;
			JsonNode apiRes = outputs.path("output").path("summary");
			
			ArrayNode rootNode = createArrayNode();
			rootNode.add(apiRes);
			
			jsonRes.setSuccess(true);
			jsonRes.setData(rootNode);
			jsonRes.setMsg("usage detail이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("usage detail 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * /api/v2/cluster . Cluster ID를 얻는다.
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cluster")
	public @ResponseBody SimpleJsonResponse getClusterId(SimpleJsonResponse jsonRes) throws Exception {
		try {
			Object response = calamariSubmit("/cluster", HttpMethod.GET);
			JsonNode clusters = (JsonNode) response;

			ObjectNode subNode = null;
			
			String clusterId = null;
			if (clusters.isArray()) {
				for (JsonNode cluster : clusters) {
					clusterId = cluster.path("id").asText();
				}
				subNode = createObjectNode();
				subNode.put("clusterId", clusterId);
			}
			
			jsonRes.setSuccess(true);
			jsonRes.setData(subNode);
			jsonRes.setMsg("cluster ID이(가) 정상적으로 조회되었습니다.");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("cluster ID 조회 중 에러가 발생하였습니다.");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

	/**
	 * <pre>
	 * Get a bucket detail
	 * </pre>
	 * @param jsonRes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cephInfo", method={ RequestMethod.GET })
	public @ResponseBody DtoJsonResponse getCephInfo(DtoJsonResponse jsonRes) throws Exception {
		try {
			CephDto dto = cephService.selectCeph();
			
			//dto.setCalamariUsername(null);
			//dto.setCalamariPassword(null);
			//dto.setS3AccessKey(null);
			//dto.setS3SecretKey(null);

			jsonRes.setSuccess(true);	
			jsonRes.setData(dto);		
			jsonRes.setMsg("Ceph 설치 정보 조회 성공");
		} catch (Exception e) {
			jsonRes.setSuccess(false);
			jsonRes.setMsg("Ceph 설치 정보 조회 실패");
			
			LOGGER.error("Unhandled Expeption has occurred. ", e);
		}
		
		return jsonRes;
	}

}
//end of HostController.java