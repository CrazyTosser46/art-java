/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.information.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.information.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface GrpcInformationMapper {
	String url = "url";

	String services = "services";

	ValueToModelMapper<GrpcInformation, Entity> toGrpcInformation = entity -> isNotEmpty(entity) ? GrpcInformation.builder()
			.url(entity.getString(url))
			.services(entity.getMap(services, PrimitiveMapping.StringPrimitive.toModel, GrpcServiceInformationMapper.toGrpcServiceInformation))
			.build() : GrpcInformation.builder().build();

	ValueFromModelMapper<GrpcInformation, Entity> fromGrpcInformation = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.stringField(url, model.getUrl())
			.mapField(services, model.getServices(), PrimitiveMapping.StringPrimitive.fromModel, GrpcServiceInformationMapper.fromGrpcServiceInformation)
			.build() : Entity.entityBuilder().build();
}
