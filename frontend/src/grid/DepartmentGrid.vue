<template>
    <div class="container-fluid">
        <DepartmentViewViewQuery @search="search"></DepartmentViewViewQuery>
        <div class="row">
            <!-- export to Excel -->
            <div class="toolbar-item col-sm-3 col-md-2">
                <excel-export-button :exportService="this.exportService" :getFlex="getFlex" />
            </div>

            <!-- export to PDF -->
            <!-- <div class="toolbar-item col-sm-3 col-md-2">
                <button class="btn btn-default btn-block" @click="exportToPdf">Export To PDF</button>
            </div> -->
        </div>

        <!-- group panel -->
        <wj-group-panel :placeholder="'Drag columns here to create groups'" :initialized="groupPanelInitialized" />

        <!-- the grid -->
        <wj-flex-grid
            :autoGenerateColumns="false"
            :allowAddNew="true"
            :allowDelete="true"
            :allowPinning="'SingleColumn'"
            :newRowAtTop="true"
            :showMarquee="true"
            :selectionMode="'MultiRange'"
            :validateEdits="false"
            :itemsSource="values"
            :initialized="flexInitialized"
        >
            <wj-flex-grid-filter :filterColumns="['id','department',]" />
            <wj-flex-grid-column binding="id" header="id" width="2*" :isReadOnly="true" />
            <wj-flex-grid-column binding="department" header="department" width="2*" :isReadOnly="true" />
        </wj-flex-grid>
        <v-col style="margin-bottom:40px;">
            <div class="text-center">
                <v-dialog
                    v-model="openDialog"
                    width="332.5"
                    fullscreen
                    hide-overlay
                    transition="dialog-bottom-transition"
                >
                    <Department :offline="offline" class="video-card" :isNew="true" :editMode="true" v-model="newValue" 
                            @add="append" v-if="tick"/>
                    <v-btn
                        style="position:absolute; top:2%; right:2%"
                        @click="closeDialog()"
                        depressed
                        icon 
                        absolute
                    >
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-dialog>
                <v-btn color="blue" fab dark large
                    style="position:fixed; bottom: 5%; right: 2%; z-index:99"
                    @click="openDialog=true;"
                >
                    <v-icon>mdi-plus</v-icon>
                </v-btn>
            </div>
        </v-col>
    </div>
</template>

<script>
import DepartmentViewQuery from '../components/DepartmentViewQuery.vue';
import Department from '../components/Department.vue'

import '@grapecity/wijmo.touch';
import '@grapecity/wijmo.vue2.grid';
import '@grapecity/wijmo.vue2.grid.grouppanel';
import '@grapecity/wijmo.vue2.grid.filter'
import '@grapecity/wijmo.vue2.input';
import * as wjCore from '@grapecity/wijmo';
import { DataMap } from '@grapecity/wijmo.grid';
import { DataService } from "./data";
import { ExportService } from "./export";
import * as wjcCore from "@grapecity/wijmo";

export default {
    name : 'department-grid',
    mixins:[Department],
    componets:{
        DepartmentViewQuery,
        Department
    },
    data: () => ({
        tick : true,
        openDialog : false,
    }),
    created(){
        this.init()
        this.dataService = new DataService();
        this.exportService = new ExportService();
        // // initializes data maps
        // this._productMap = this._buildDataMap(this.dataService.getProducts());
        // this._countryMap = new DataMap(this.dataService.getCountries(), 'id', 'name');
        // this._colorMap = this._buildDataMap(this.dataService.getColors());
        // // initializes cell templates
        // this.historyCellTemplate = CellMaker.makeSparkline({
        //     markers: SparklineMarkers.High | SparklineMarkers.Low,
        //     maxPoints: 25,
        //     label: 'price history',
        // });
        // this.ratingCellTemplate = CellMaker.makeRating({
        //     range: [1, 5],
        //     label: 'rating'
        // });
    }, 
    beforeDestroy() {
        this.exportService.cancelExcelExport();
    },
    methods:{
         groupPanelInitialized: function (ctl) {
            this.groupPanel = ctl;
            if (this.flex) {
                this.groupPanel.grid = this.flex;
            }
        },
        exportToPdf: function() {
            this.exportService.exportToPdf(this.flex, {
                countryMap: this._countryMap,
                colorMap: this._colorMap,
                historyCellTemplate: this.historyCellTemplate
            });
        },
        _buildDataMap: function (items) {
            const map = [];
            for (let i = 0; i < items.length; i++) {
                map.push({ key: i, value: items[i] });
            }
            return new DataMap(map, 'key', 'value');
        },
        getFlex: function () {
            return this.flex;
        },
        flexInitialized: function(flexgrid) {
            this.flex = flexgrid;
            let sd = new wjcCore.SortDescription("country", true);
            flexgrid.collectionView.sortDescriptions.push(sd);
            flexgrid.collectionView.currentChanged.addHandler(
                this._updateCurrentInfo.bind(this)
            );
            this._updateCurrentInfo();
        },
        departmentId(href){
            if(href){
                return href.split('/')[2]
            }
            return null;
        },
        getChangeCls: function (value) {
            if (wjCore.isNumber(value)) {
                if (value > 0) {
                    return 'change-up';
                }
                if (value < 0) {
                    return 'change-down';
                }
            }
            return '';
        },
    },
    filters: {
        safeCurrency: function (value) {
            if (wjCore.isNumber(value)) {
                return wjCore.Globalize.formatNumber(value, 'c');
            }
            if (!wjCore.isUndefined(value) && value !== null) {
                return wjCore.changeType(value, wjCore.DataType.String);
            }
            return '';
        }
    },
}
</script>

<style>
    @import "~@grapecity/wijmo.styles/wijmo.css";
    @import "~bootstrap/dist/css/bootstrap.css";
    @import url('https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.1.0/css/flag-icon.css');

    body {
        font-size:1.5em;
        font-family: -apple-system,BlinkMacSystemFont,"Segoe UI Light",Roboto,Oxygen-Sans,Ubuntu,Cantarell,"Helvetica Neue",sans-serif;
    }

    .toolbar-item {
        margin-bottom: 6px;
    }

    .wj-flexgrid {
        height: 330px;
    }

    .wj-flexgrid .wj-cell {
        padding: 7px;
        border:none;
    }

    .wj-cell.wj-state-invalid:not(.wj-header)::after {
        top: -14px;
        border: 14px solid transparent;
        border-right-color: red;
    }

    .flag-icon {
        box-shadow: 1px 1px 4px rgba(0,0,0,0.4);
    }

    .color-tile {
        display: inline-block;
        position: relative;
        width: 1em;
        height: 1em;
        border-radius: 50%;
        box-shadow: 1px 1px 4px rgba(0,0,0,0.4);
        vertical-align: middle;
    }

    .change-up {
        color: darkgreen;
    }
    .change-up:after {
        content: '\25b2';
    }
    .change-down {
        color: darkred;
    }
    .change-down:after {
        content: '\25bc';
    }

    .cell-rating {
        font-size: 12px;
    }

    .wj-flexgrid .wj-detail {
        padding: 4px 16px;
    }

    .wj-detail h3 {
        margin:10px 0;
    }
</style>