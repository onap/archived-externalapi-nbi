/**
 * Copyright (c) 2020 TechMahindra
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.onap.nbi.apis.serviceorder.utils;

import java.util.Map;

import org.onap.nbi.apis.serviceorder.model.orchestrator.ServiceOrderItemInfo;

public class MacroServiceUtils {
	public static boolean isMacroService(ServiceOrderItemInfo serviceOrderItemInfo) {
		Map<String, Object> sdcInfos = serviceOrderItemInfo.getCatalogResponse();
		boolean macroService = false;
		if (sdcInfos.get("instantiationType") != null) {
			String instantiationType = ((String) sdcInfos.get("instantiationType")).toLowerCase();
			if (instantiationType.equals("macro")) {
				macroService = true;
			}
		}
		return macroService;
	}

}
