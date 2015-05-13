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

package org.wso2.carbon.device.mgt.iot.enroll;

import org.wso2.carbon.device.mgt.common.Device;
import org.wso2.carbon.device.mgt.common.DeviceIdentifier;
import org.wso2.carbon.device.mgt.iot.common.IOTAPIException;



/**
 * @author ayyoobhamza
 *
 */
public interface DeviceManagement {
	
	
	public boolean addNewDevice(Device device) throws IOTAPIException;
	public boolean removeDevice(DeviceIdentifier deviceIdentifier) throws IOTAPIException;
	public boolean update(Device device) throws IOTAPIException;
	public Device getDevice(DeviceIdentifier deviceIdentifier) throws IOTAPIException;
	public boolean isExist(DeviceIdentifier deviceIdentifier) throws IOTAPIException;
	public boolean isExist(String owner,DeviceIdentifier deviceIdentifier) throws IOTAPIException;

}
