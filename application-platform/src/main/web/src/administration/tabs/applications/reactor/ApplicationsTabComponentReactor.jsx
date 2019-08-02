/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import {registry} from "../../../../framework/registry/Registry";
import {loadApplications, saveProject} from "../../../../service/AdministrationService";
import isEmpty from 'licia/isEmpty'

export const reactors = {
    onMount: () => loadApplications(applications => {
        if (isEmpty(applications)) return;
        registry.applicationsTab.store.applications = new Set(applications);
        registry.applicationsTab.store.apply();
    }),


    onInputNewApplication: (application) => registry.applicationsTab.store.newApplication = application,

    onInputNewProjectGroup: (group) => registry.applicationsTab.store.newProjectGroup = group,

    onInputNewProject: (project) => registry.applicationsTab.store.newProject = project,


    onSelectApplication: (application) => registry.applicationsTab.controller.selectApplication(application),

    onSelectProjectGroup: (group) => registry.applicationsTab.controller.selectProjectGroup(group),

    onSelectProject: (project) => registry.applicationsTab.controller.selectProject(project),


    onAddApplication: () => {
        if (!registry.applicationsTab.store.addApplication(registry.applicationsTab.store.newApplication)) return;
        registry.applicationsTab.controller.selectApplication(registry.applicationsTab.store.newApplication);
    },

    onAddProjectGroup: () => {
        if (!registry.applicationsTab.store.addProjectGroup(registry.applicationsTab.store.newProjectGroup)) return;
        registry.applicationsTab.controller.selectProjectGroup(registry.applicationsTab.store.newProjectGroup);
    },

    onAddProject: () => {
        if (!registry.applicationsTab.store.addProject(registry.applicationsTab.store.newProject)) return;
        registry.applicationsTab.controller.selectProject(registry.applicationsTab.store.newProject);
    },


    onDeleteApplication: () => {
        if (!registry.applicationsTab.store.deleteApplication(registry.applicationsTab.store.selectedApplication)) return;
        registry.applicationsTab.store.selectedApplication = null;
        registry.applicationsTab.store.selectedProject = null;
        registry.applicationsTab.store.selectedProjectGroup = null;
        registry.applicationsTab.store.apply();
    },

    onDeleteProjectGroup: () => {
        if (!registry.applicationsTab.store.deleteProjectGroup(registry.applicationsTab.store.selectedProjectGroup)) return;
        registry.applicationsTab.store.selectedProject = null;
        registry.applicationsTab.store.selectedProjectGroup = null;
        registry.applicationsTab.store.apply();
    },

    onDeleteProject: () => {
        if (!registry.applicationsTab.store.deleteProject(registry.applicationsTab.store.selectedProject)) return;
        registry.applicationsTab.store.selectedProject = null;
        registry.applicationsTab.store.apply();
    },

    onSave: () => {
        if (!registry.projectConfiguration) {
            return;
        }
        saveProject(registry.applicationsTab.store.selectedApplication, registry.applicationsTab.store.selectedProjectGroup, registry.projectConfiguration.store.project);
        registry.projectConfiguration.store.apply()
    }
};