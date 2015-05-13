/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.device.mgt.iot.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.device.mgt.iot.common.IOTAPIException;
import org.wso2.carbon.device.mgt.iot.enroll.UserManagement;
import org.wso2.carbon.device.mgt.iot.utils.IoTConfiguration;
import org.wso2.carbon.device.mgt.user.common.User;

@Path("/UserManager")
public class UserManager {
	private static Log log = LogFactory.getLog(UserManager.class);

	// @Path("/UserRegister")
	// @PUT
	// public void userRegister(@QueryParam("username") String username,
	// @QueryParam("password") String password,
	// @QueryParam("firstName") String firstName,
	// @QueryParam("lastName") String lastName,
	// @QueryParam("email") String email,
	// @QueryParam("roles") String[] roles,
	// @Context HttpServletResponse response) throws ConfigurationException,
	// InstantiationException,
	// IllegalAccessException {
	//
	//
	//
	//
	// UserManagement userManagement =
	// IoTConfiguration.getInstance().getUserManagementImpl();
	// if(userManagement.isExist(username)){
	// response.setStatus(409);
	// return;
	//
	// }
	// if(username.equals(userManagement.getAnonymousUserName())){
	// response.setStatus(400);
	// return;
	// }
	//
	// User user = new User();
	// user.setUsername(username);
	// user.setPassword(password);
	// user.setFirstname(firstName);
	// user.setLastname(lastName);
	// user.setEmail(email);
	// for (String role : roles) {
	// user.addRole(role);
	//
	// }
	//
	//
	//
	//
	// boolean added = userManagement.addNewUser(user);
	//
	// if (added) {
	//
	// response.setStatus(200);
	// } else {
	// response.setStatus(409);
	//
	// }
	// }

	// @Path("/RemoveUser")
	// @DELETE
	// public void removeUser(@QueryParam("username") String username,
	// @Context HttpServletRequest request,
	// @Context HttpServletResponse response) throws InstantiationException,
	// IllegalAccessException,
	// ConfigurationException, IOTAPIException {
	//
	// boolean status = authorizedCheck(username, request, response);
	// if (status) {
	// UserManagement userManagement =
	// IoTConfiguration.getInstance().getUserManagementImpl();
	// boolean removed = userManagement.removeUser(username);
	// if (removed) {
	//
	// response.setStatus(200);
	// } else {
	// response.setStatus(409);
	//
	// }
	// }
	//
	// }

	// @Path("/UpdateUser")
	// @POST
	// public void updateUser(@QueryParam("username") String username,
	// @QueryParam("firstName") String firstName,
	// @QueryParam("lastName") String lastName,
	// @QueryParam("email") String email, @QueryParam("roles") String[] roles,
	// @Context HttpServletRequest request,
	// @Context HttpServletResponse response) throws InstantiationException,
	// IllegalAccessException,
	// ConfigurationException {
	//
	// boolean status = authorizedCheck(username, request, response);
	// if (status) {
	// User user = new User();
	//
	// user.setUserName(username);
	//
	// user.setFirstname(firstName);
	// user.setEmail(email);
	// user.setLastname(lastName);
	// for (String role : roles) {
	// user.addRole(role);
	//
	// }
	//
	// UserManagement userManagement =
	// IoTConfiguration.getInstance().getUserManagementImpl();
	// boolean updated = userManagement.updateUser(user);
	// if (updated) {
	// response.setStatus(200);
	// } else {
	// response.setStatus(409);
	// }
	//
	// }
	//
	// }

	@Path("/GetUser")
	@GET
	@Consumes("application/json")
	public User getUser(@QueryParam("username") String username,
	                    @Context HttpServletRequest request, @Context HttpServletResponse response)
	                                                                                               throws InstantiationException,
	                                                                                               IllegalAccessException,
	                                                                                               ConfigurationException,
	                                                                                               IOTAPIException {

		boolean status = authorizedCheck(username, request, response);
		if (status) {
			UserManagement userManagement = IoTConfiguration.getInstance().getUserManagementImpl();
			User user = userManagement.getUser(username);
			if (user != null) {

				response.setStatus(200);
				return user;
			} else {
				response.setStatus(409);

			}
		}
		return null;

	}

	@Path("/Login")
	@POST
	public boolean login(@QueryParam("username") String username,
	                     @QueryParam("password") String password,
	                     @Context HttpServletRequest request, @Context HttpServletResponse response)
	                                                                                                throws ConfigurationException,
	                                                                                                InstantiationException,
	                                                                                                IllegalAccessException,
	                                                                                                IOTAPIException {

		UserManagement userManagement = IoTConfiguration.getInstance().getUserManagementImpl();

		boolean authenticated = userManagement.isAuthenticated(username, password);

		if (authenticated) {
			request.getSession(true);
			request.getSession().setAttribute("user", userManagement.getUser(username));
			response.setStatus(200);
			return true;

		} else {

			response.setStatus(401);
		}

		return false;

	}

	public boolean authorizedCheck(String username, HttpServletRequest request,
	                               @Context HttpServletResponse response) {

		User loggedInUser = (User) request.getSession().getAttribute("user");
		if (loggedInUser == null) {
			response.setStatus(403);
			return false;
		}

		if (!(loggedInUser.getUserName().equals(username))) {
			// List userRoles = loggedInUser.getRoles();
			boolean authorized = false;
			// for (int i = 0; i < userRoles.size(); i++) {

			// if ("admin".equals((String) userRoles.get(i))) {
			if ("admin".equals(loggedInUser.getRoleName())) {
				authorized = true;
				return true;
			}
			// }
			if (!authorized) {
				response.setStatus(401);
				return false;
			}
		}
		return true;
	}

}