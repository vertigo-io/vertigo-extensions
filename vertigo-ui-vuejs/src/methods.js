import Quasar from "quasar"
import { sortDate } from "quasar/src/utils/sort.js"
import { isNumber, isDate } from "quasar/src/utils/is.js"

export default {
    onAjaxError: function (response) {
        //Quasar Notif Schema
        let notif = {
            type: 'negative',
            message: 'Network Error.',
            multiLine: true,
            icon: 'warning',
            timeout: 2500,
        }

        //Setup Error Message //if response was an error
        if (Object.prototype.hasOwnProperty.call(response, 'message')) {
            notif.message = response.message
        }
        //Setup Generic Response Messages
        if (response.status === 401) {
            notif.message = 'UnAuthorized, you may login with an authorized account'
            this.$root.$emit('logout') //Emit Logout Event
        } else if (response.status === 403) {
            notif.message = 'Forbidden, your havn&quote;t enought rights'
        } else if (response.status === 404) {
            notif.message = 'API Route is Missing or Undefined'
        } else if (response.status === 405) {
            notif.message = 'API Route Method Not Allowed'
        } else if (response.status === 422) {
            //Validation Message
            notif.message = '';
            Object.keys(response.data).forEach(function (key) {
                this.$data.uiMessageStack[key] = response.data[key];
            }.bind(this));
        } else if (response.status >= 500) {
            notif.message = 'Server Error'
        }
        if (response.statusText) {
            notif.message = response.statusText
        }
        //Try to Use the Response Message
        if (Object.prototype.hasOwnProperty.call(response, 'data')) {
            if (Object.prototype.hasOwnProperty.call(response.data, 'message') && response.data.message && response.data.message.length > 0) {
                notif.message = response.data.message
            } else if (Object.prototype.hasOwnProperty.call(response.data, 'globalErrors') && response.data.globalErrors && response.data.globalErrors.length > 0) {
                notif.message = response.data.globalErrors.join('<br/>\n ');
            }
        }
        //Send the notif
        if (notif.message.length > 0) {
            this.$q.notify(notif);
        }
    },

    getSafeValue: function (objectkey, fieldKey, subFieldKey) {
        if (this.$data.vueData[objectkey] && this.$data.vueData[objectkey][fieldKey]) {
            return this.$data.vueData[objectkey][fieldKey][subFieldKey];
        }
        return null;
    },

    transformListForSelection: function (list, valueField, labelField) {
        return this.$data.vueData[list].map(function (object) {
            return { value: object[valueField], label: object[labelField].toString() } // a label is always a string
        });
    },

    paginationAndSortHandler: function (params) {
        var pagination = params.pagination;
        let componentStates = this.$data.componentStates;
        let vueData = this.$data.vueData;
        var oldPagination = componentStates[pagination.componentId].pagination;
        if (oldPagination.sortBy != pagination.sortBy || oldPagination.descending != pagination.descending) {
            if (pagination.sortBy) {
                // it's a sort
                if (pagination.sortUrl) {
                    //order call the server
                    pagination.page = 1 //reset pagination
                    this.$http.post(pagination.sortUrl, this.objectToFormData({ sortFieldName: pagination.sortBy, sortDesc: pagination.descending, CTX: this.$data.vueData.CTX }))
                        .then(function (response) {
                            vueData[pagination.listKey] = response.data.model[pagination.listKey];
                            this.$data.vueData.CTX = response.data.model['CTX'];
                        }.bind(this));
                } else {
                    //do locally
                    this.$refs[pagination.componentId].sortMethod.apply(this.$refs[pagination.componentId], [vueData[pagination.listKey], pagination.sortBy, pagination.descending])
                }
            } // if we reset the sort we do nothing
        }
        // otherwise it's pagination or filter : do it locally
        // nothing to do everything is done by the paginatedData function
        componentStates[pagination.componentId].pagination = pagination;
    },
    paginatedData: function (list, componentId) {
        let componentStates = this.$data.componentStates;
        var pagination = componentStates[componentId].pagination;
        if (pagination.rowsPerPage != 0) { // not all
            var firstRowIndex = (pagination.page - 1) * pagination.rowsPerPage;
            var lastRowIndex = pagination.page * pagination.rowsPerPage;
            return this.$data.vueData[list].slice(firstRowIndex, lastRowIndex);
        }
        return this.$data.vueData[list];
    },


    sortCiAi: function (data, sortBy, descending) {
        const col = this.colList.find(def => def.name === sortBy)
        if (col === void 0 || col.field === void 0) {
            return data
        }

        const
            dir = descending === true ? -1 : 1,
            val = typeof col.field === 'function'
                ? v => col.field(v)
                : v => v[col.field]

        const collator = new Intl.Collator();

        return data.sort((a, b) => {
            let A = val(a),
                B = val(b)

            if (A === null || A === void 0) {
                return -1 * dir;
            }
            if (B === null || B === void 0) {
                return 1 * dir;
            }
            if (col.sort !== void 0) {
                return col.sort(A, B, a, b) * dir;
            }
            if (isNumber(A) === true && isNumber(B) === true) {
                return (A - B) * dir;
            }
            if (isDate(A) === true && isDate(B) === true) {
                return sortDate(A, B) * dir;
            }
            if (typeof A === 'boolean' && typeof B === 'boolean') {
                return (A - B) * dir;
            }

            [A, B] = [A, B].map(s => (s + '').toLocaleString());

            return collator.compare(A, B) * dir;
        })
    },

    selectedFunction: function (object, field, item /*, keyboard*/) {
        this.$data.vueData[object][field] = item.value;
    },

    searchAutocomplete: function (list, valueField, labelField, componentId, url, terms, update, abort) {
        if (terms.length < 2) {
            abort();
            return
        }
        this.$http.post(url, this.objectToFormData({ terms: terms, list: list, valueField: valueField, labelField: labelField, CTX: this.$data.vueData.CTX }))
            .then(function (response) {
                var finalList = response.data.map(function (object) {
                    return { value: object[valueField], label: object[labelField].toString() } // a label is always a string
                });
                update(function () {
                    this.$data.componentStates[componentId].options = finalList;
                }.bind(this));
            }.bind(this))
            .catch(function (error) {
                this.$q.notify(error.response.status + ":" + error.response.statusText);
                update([]);
            });

    },
    loadAutocompleteById: function (list, valueField, labelField, componentId, url, objectName, fieldName) {
        //Method use when value(id) is set by another way : like Ajax Viewcontext update, other component, ...
        //if options already contains the value (id) : we won't reload.
        if (!this.$data.vueData[objectName][fieldName] || (this.$data.componentStates[componentId].options
            .filter(function (option) { return option.value === this.$data.vueData[objectName][fieldName] }.bind(this)).length > 0)) {
            return
        }
        this.$data.componentStates[componentId].loading = true;
        this.$data.componentStates[componentId].options.push({ 'value': this.$data.vueData[objectName][fieldName], 'label': '' })
        this.$http.post(url, this.objectToFormData({ value: this.$data.vueData[objectName][fieldName], list: list, valueField: valueField, labelField: labelField, CTX: this.$data.vueData.CTX }))
            .then(function (response) {
                var finalList = response.data.map(function (object) {
                    return { value: object[valueField], label: object[labelField].toString() } // a label is always a string
                });
                this.$data.componentStates[componentId].options.pop();
                this.$data.componentStates[componentId].options = this.$data.componentStates[componentId].options.concat(finalList);
            }.bind(this))
            .catch(function (error) {
                this.$data.componentStates[componentId].options.pop();
                this.$q.notify(error.response.status + ":" + error.response.statusText);
            }.bind(this))
            .then(function () {// always executed
                this.$data.componentStates[componentId].loading = false;
            }.bind(this));
    },

    decodeDate: function (value, format) {
        if (value === Quasar.utils.date.formatDate(Quasar.utils.date.extractDate(value, 'DD/MM/YYYY'), 'DD/MM/YYYY')) {
            return Quasar.utils.date.formatDate(Quasar.utils.date.extractDate(value, 'DD/MM/YYYY'), format);
        } else {
            return value;
        }
    },

    encodeDate: function (newValue, format) {
        if (newValue === Quasar.utils.date.formatDate(Quasar.utils.date.extractDate(newValue, format), format)) {
            return Quasar.utils.date.formatDate(Quasar.utils.date.extractDate(newValue, format), 'DD/MM/YYYY');
        } else {
            return newValue;
        }
    },

    decodeDatetime: function (value, format) {
        if (value === Quasar.utils.date.formatDate(Quasar.utils.date.extractDate(value, 'DD/MM/YYYY HH:mm'), 'DD/MM/YYYY HH:mm')) {
            return Quasar.utils.date.formatDate(Quasar.utils.date.extractDate(value, 'DD/MM/YYYY HH:mm'), format);
        } else {
            return value;
        }
    },

    encodeDatetime: function (newValue, format) {
        if (newValue === Quasar.utils.date.formatDate(Quasar.utils.date.extractDate(newValue, format), format)) {
            return Quasar.utils.date.formatDate(Quasar.utils.date.extractDate(newValue, format), 'DD/MM/YYYY HH:mm');
        } else {
           return newValue;
        }
    },

    sortDatesAsString: function (format) {
        return function (date1, date2) {
            return (Quasar.utils.date.extractDate(date1, format).getTime() > Quasar.utils.date.extractDate(date2, format).getTime()) ? 1 : -1;
        }
    },

    goTo: function (url) {
        window.location = url;
    },

    openModal: function (modalId, url, params) {
        if (url) {
            var finalUrl = url;
            if (params && Object.keys(params).length > 0) {
                var paramsString = Object.keys(params).map(function (key) {
                    return key + '=' + params[key];
                }).join("&");
                finalUrl = finalUrl + '?' + paramsString;
            }
            this.$data.componentStates[modalId].srcUrl = finalUrl;
        }
        this.$data.componentStates[modalId].opened = true;
    },

    toogleFacet: function (facetCode, facetValueCode, contextKey) {
        let vueData = this.$data.vueData;
        var multiple = false;
        vueData[contextKey + "_facets"].forEach(function (facet) {
            if (facet.code === facetCode) {
                // get the right facet 
                multiple = facet.multiple;
            }
        })
        var selectedFacetValues = vueData[contextKey + "_selectedFacets"][facetCode]
        if (selectedFacetValues) {
            if (selectedFacetValues.includes(facetValueCode)) {
                if (multiple) {
                    selectedFacetValues.splice(selectedFacetValues.indexOf(facetValueCode), 1);
                } else {
                    selectedFacetValues.splice(0);
                }
            } else {
                selectedFacetValues.push(facetValueCode);
            }
        } else {
            vueData[contextKey + "_selectedFacets"][facetCode] = [facetValueCode];
        }
        this.search(contextKey);
    },

    search: Quasar.utils.debounce(function (contextKey) {
        let componentStates = this.$data.componentStates;
        let vueData = this.$data.vueData;
        var selectedFacetsContextKey = contextKey + "_selectedFacets";
        var criteriaContextKey = vueData[contextKey + '_criteriaContextKey'];
        var params = this.vueDataParams([criteriaContextKey]);
        params.append('selectedFacets', JSON.stringify(vueData[selectedFacetsContextKey]));

        var searchUrl = componentStates[contextKey + 'Search'].searchUrl;
        var collectionComponentId = componentStates[contextKey + 'Search'].collectionComponentId;

        if (componentStates[collectionComponentId].pagination && componentStates[collectionComponentId].pagination.sortBy) {
            var collectionPagination = componentStates[collectionComponentId].pagination;
            params.append('sortFieldName', collectionPagination.sortBy);
            params.append('sortDesc', collectionPagination.descending);
        }
        this.httpPostAjax(searchUrl, params, {
            onSuccess: function (response) {
                if (componentStates[collectionComponentId].pagination) {
                    var collectionPagination = componentStates[collectionComponentId].pagination;
                    collectionPagination.page = 1 // reset page
                    collectionPagination.rowsNumber = response.body.model[contextKey + '_list'].length
                }
            }
        });
    }, 400),

    showMore: function (componentId) {
        let componentStates = this.$data.componentStates;
        var showMoreCount = componentStates[componentId].pagination.showMoreCount;
        if (showMoreCount) {
            // nothing to do
        } else {
            showMoreCount = 1;
            componentStates[componentId].pagination.showMoreCount = showMoreCount;
        }
        componentStates[componentId].pagination.rowsPerPage = componentStates[componentId].pagination.rowsPerPage / showMoreCount * (showMoreCount + 1);
    },
    vueDataToArray(value) {
        if(Array.isArray(value)) {
            return value;
        } else if(value) {
            return [value];
        }
        return [];
    },
    obtainVueDataAccessor(referer, object, field, rowIndex) {
        if(field!=null && field!='null') {
            if(rowIndex!=null) {
                return {
                    get : function() {
                        return referer.$data.vueData[object][rowIndex][field];
                    }, 
                    set : function(newData) {
                        referer.$data.vueData[object][rowIndex][field] = newData;
                    }
                }
            } else {
                return {           
                    get : function() {
                        return referer.$data.vueData[object][field];
                    }, 
                    set : function(newData) {
                        referer.$data.vueData[object][field] = newData;
                    }
                }
            }
        } else {
            return {
                get : function() {
                    return referer.$data.vueData[object];
                }, 
                set : function(newData) {
                    referer.$data.vueData[object] = newData;
                }
            }
        }
    },
    uploader_changeIcon () {
        this.$q.iconSet.uploader.removeUploaded = 'delete_sweep'
        this.$q.iconSet.uploader.done = 'delete'
    },
    uploader_mounted(componentId, object, field, rowIndex) {
        this.uploader_changeIcon();
        var component = this.$refs[componentId];
        //must removed duplicate
        component.vueDataAccessor = this.obtainVueDataAccessor(this, object, field, rowIndex);
        var vueDataAccessor = component.vueDataAccessor;
        var curValue = vueDataAccessor.get();
        if(!Array.isArray(curValue)) {
            vueDataAccessor.set(this.vueDataToArray(curValue));
        }        
        vueDataAccessor.set(vueDataAccessor.get().filter(function(item, pos, self) {
            return self.indexOf(item) == pos;
        }));
        vueDataAccessor.get().forEach(function (uri) {
        var xhrParams = {};
        xhrParams[component.fieldName] = uri;
            this.$http.get(component.url, { params: xhrParams, credentials: component.withCredentials })
            .then(function (response) { //Ok
                var fileData = response.data;
                if(component.files.some(file => file.name === fileData.name)){
                    console.warn("Component doesn't support duplicate file ", fileData);
                } else {  
                    fileData.__sizeLabel = Quasar.utils.format.humanStorageSize(fileData.size);
                    fileData.__progressLabel = '100%';
                    component.files.push(fileData);
                    component.uploadedFiles.push(fileData);
                    this.uploader_forceComputeUploadedSize(componentId);
                }
            }.bind(this))
            .catch(function (error) { //Ko
                this.$q.notify(error.response.status + ":" + error.response.statusText + " Can't load file "+uri);
            }.bind(this));
        }.bind(this));
    },
    uploader_dragenter(componentId) {
        let componentStates = this.$data.componentStates;
        componentStates[componentId].dragover = true;
    },
    uploader_dragleave(componentId) {
        let componentStates = this.$data.componentStates;
        componentStates[componentId].dragover = false;
    },
    uploader_drop(event, componentId) {
        var component = this.$refs[componentId];
        component.addFiles(event.dataTransfer.files);
    },
    uploader_forceComputeUploadedSize: function (componentId) {
        var component = this.$refs[componentId];
        //recompute totalSize
        component.uploadedSize = 0;
        component.uploadedFiles.forEach(function (file) { component.uploadedSize += file.size;});
        component.uploadSize = component.uploadedSize;
        component.queuedFiles.forEach(function (file) { component.uploadSize += file.size;});
        
    },
    uploader_humanStorageSize: function (size) {
        return Quasar.utils.format.humanStorageSize(size);
    },
    uploader_addedFile: function (isMultiple, componentId) {
        if (!isMultiple) {
             var component = this.$refs[componentId];
             var vueDataAccessor = component.vueDataAccessor;
             component.removeUploadedFiles();
             vueDataAccessor.set([]);
        }
    },
    uploader_uploadedFiles: function (uploadInfo, componentId) {
        var component = this.$refs[componentId];
        var vueDataAccessor = component.vueDataAccessor;
        uploadInfo.files.forEach(function (file) {
            file.fileUri = file.xhr.response;
            vueDataAccessor.get().push(file.fileUri);
        }.bind(this));
    },
    uploader_failedFiles: function (uploadInfo) {
        uploadInfo.files.forEach(function (file) {
            this.onAjaxError({
                status : file.xhr.status,
                statusText : file.xhr.statusText,
                data : JSON.parse(file.xhr.response)
                }
            );
            //server can return : a response with a uiMessageStack object or directly the uiMessageStack
            /*let uiMessageStack = response.globalErrors?response:response.uiMessageStack;
            Object.keys(uiMessageStack).forEach(function (key) {
                this.$data.uiMessageStack[key] = uiMessageStack[key];
            }.bind(this));*/
        }.bind(this));
    },
    uploader_removeFiles: function (removedFiles, componentId) {
        var component = this.$refs[componentId];
        var vueDataAccessor = component.vueDataAccessor;
        var dataFileUris = vueDataAccessor.get();
        removedFiles.forEach(function (removedFile) {
            if(removedFile.fileUri) { //if file is serverside
            var indexOfFileUri = dataFileUris.indexOf(removedFile.fileUri);
            var xhrParams = {};
            xhrParams[component.fieldName] = removedFile.fileUri;
                this.$http.delete(component.url, { params: xhrParams, credentials: component.withCredentials })
                    .then(function (/*response*/) { //Ok
                        if (component.multiple) {
                            dataFileUris.splice(indexOfFileUri, 1);
                        } else {
                            dataFileUris.splice(0);
                        }
                        this.uploader_forceComputeUploadedSize(componentId);
                    }.bind(this))
                    .catch(function (error) { //Ko
                        this.$q.notify(error.response.status + ":" + error.response.statusText + " Can't remove temporary file");
                    }.bind(this));
            }
        }.bind(this));
    },
    

    httpPostAjax: function (url, paramsIn, options) {
        let vueData = this.$data.vueData;
        let uiMessageStack = this.$data.uiMessageStack;
        let params = this.isFormData(paramsIn) ? paramsIn : this.objectToFormData(paramsIn);
        params.append('CTX', vueData.CTX);
        this.$http.post(url, params).then(function (response) {
            if (response.data.model.CTX) {
                vueData.CTX = response.data.model.CTX;
            }
            Object.keys(response.data.model).forEach(function (key) {
                if ('CTX' != key) {
                    vueData[key] = response.data.model[key];
                }
            });
            Object.keys(response.data.uiMessageStack).forEach(function (key) {
                uiMessageStack[key] = response.data.uiMessageStack[key];
            });
            if (options && options.onSuccess) {
                options.onSuccess.bind(this).apply(response);
            }
        }.bind(this)).catch(function (error) {
            if (options && options.onError) {
                options.onError.bind(this).apply(error.response);
            }
        });
    },

    hasFieldsError: function (object, field, rowIndex) {
        const fieldsErrors = this.$data.uiMessageStack.objectFieldErrors;
        if (fieldsErrors) {
        var objectName = rowIndex!=null?object+'['+rowIndex+']':object;
            return Object.prototype.hasOwnProperty.call(fieldsErrors, objectName) &&
            fieldsErrors[objectName] && Object.prototype.hasOwnProperty.call(fieldsErrors[objectName], field) && fieldsErrors[objectName][field].length > 0
        }
        return false;
    },

    getErrorMessage: function (object, field, rowIndex) {
        const fieldsErrors = this.$data.uiMessageStack.objectFieldErrors;
        if (fieldsErrors) {
            var objectName = rowIndex!=null?object+'['+rowIndex+']':object;
            if (Object.prototype.hasOwnProperty.call(fieldsErrors, objectName) &&
            fieldsErrors[objectName] && Object.prototype.hasOwnProperty.call(fieldsErrors[objectName], field)) {
                return fieldsErrors[objectName][field].join(', ');
            }
        } else {
            return '';
        }
    },

    vueDataParams: function (keys) {
        var params = new FormData();
        for (var i = 0; i < keys.length; i++) {
            var contextKey = keys[i];
            var vueDataValue = this.$data.vueData[contextKey];
            if (vueDataValue && typeof vueDataValue === 'object' && Array.isArray(vueDataValue) === false) {
                // object
                Object.keys(vueDataValue).forEach(function (propertyKey) {
                    if (!propertyKey.startsWith("_")) {
                        // _ properties are private and don't belong to the serialized entity
                        if (Array.isArray(vueDataValue[propertyKey])) {
                            vueDataValue[propertyKey].forEach(function (value, index) {
                                if (vueDataValue[propertyKey][index] && typeof vueDataValue[propertyKey][index] === 'object') {
                                    params.append('vContext[' + contextKey + '][' + propertyKey + ']', vueDataValue[propertyKey][index]['_v_inputValue']);
                                } else {
                                    params.append('vContext[' + contextKey + '][' + propertyKey + ']', vueDataValue[propertyKey][index]);
                                }
                            });
                        } else {
                            if (vueDataValue[propertyKey] && typeof vueDataValue[propertyKey] === 'object') {
                                params.append('vContext[' + contextKey + '][' + propertyKey + ']', vueDataValue[propertyKey]['_v_inputValue']);
                            } else {
                                params.append('vContext[' + contextKey + '][' + propertyKey + ']', vueDataValue[propertyKey]);
                            }
                        }
                    }
                });
            } else {
                //primitive
                params.append('vContext[' + contextKey + ']', vueDataValue);
            }
        }
        return params;

    },

    objectToFormData: function (object) {
        const formData = new FormData();
        Object.keys(object).forEach(key => formData.append(key, object[key]));
        return formData;
    },
    
    isFormData: function(val) {
        return (typeof FormData !== 'undefined') && (val instanceof FormData);
    }

}
