import {Button} from "primereact/button";
import {useContext, useEffect, useState} from "react";
import {UserContext} from "../context/UserContext.jsx";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {getUser} from "../api/userApi.js";
import {getSurveyResults} from "../api/surveyApi.js";
import SurveyResult from "./SurveyResult.jsx";

const Surveys = ({toast, setVisible}) => {

    const [user, setUser] = useContext(UserContext);
    const [surveys, setSurveys] = useState([]);
    const [result, setResult] = useState(null);

    useEffect(() => {
        const _surveys = (user && user.surveys) ? user.surveys : [];
        setSurveys(_surveys);
    }, [user]);

    if (result) {
        return <SurveyResult result={result} setResult={setResult} />
    }

    const refreshSurveys = async () => {
        const _user = await getUser();
        if (_user.success) {
            setUser(_user.body);
        }
    }

    const closeSurvey = async (survey) => {
        // TODO Function should inverse the closed state of survey

        const _user = await getUser();
        if (_user.success) {
            setUser(_user.body);
        }
    }

    const getResults = async (survey) => {
        const status = await getSurveyResults(survey.surveyId);
        if (status.success) {
            setResult(status.body)
        } else {
            setResult(null);
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Results Error',
                detail: status.body.message,
            });
        }
    }

    const surveyCodeTemplate = (rowData) => {
        return (
            <div className="flex align-items-center">
                {rowData.surveyId}
                <Button icon="pi pi-clipboard" className='p-button-text p-button-rounded' style={{boxShadow: "none"}}
                        onClick={() => navigator.clipboard.writeText(rowData.surveyId)} tooltip="Copy Code"/>
            </div>
        )
    }

    const surveyClosedTemplate = (rowData) => {
        return (
            <>
                {
                    (rowData.closed) ?
                        <Button icon="pi pi-lock" className='p-button-text p-button-danger p-button-rounded'
                                style={{boxShadow: "none"}}
                                onClick={() => navigator.clipboard.writeText(rowData.surveyId)}
                                tooltip="Click to open survey"/>
                        :
                        <Button icon="pi pi-lock-open" className='p-button-text p-button-rounded'
                                style={{boxShadow: "none"}}
                                onClick={() => navigator.clipboard.writeText(rowData.surveyId)}
                                tooltip="Click to close survey"/>
                }
            </>
        )
    }

    const surveyResultsTemplate = (rowData) => {

        return (
            <>
                <Button icon="pi pi-chart-bar" className='p-button-text p-button-rounded'
                        style={{boxShadow: "none"}}
                        onClick={async () => await getResults(rowData)}
                        tooltip="Get Results"/>
            </>
        )
    }

    const header = (
        <div className="flex flex-wrap align-items-center justify-content-between gap-2">
            <span className="text-2xl text-primary font-bold">My Surveys</span>
            <Button icon="pi pi-refresh" rounded raised style={{boxShadow: "none"}} onClick={() => refreshSurveys()}
                    tooltip="Refresh"/>
        </div>
    );

    return (
        <>
            <Button label="Back" icon="pi pi-arrow-circle-left" size="small" className="absolute top-0 left-0 m-4"
                    style={{boxShadow: "none"}} onClick={() => setVisible(false)}/>
            <div className="flex justify-content-center p-8">
                <DataTable value={surveys} header={header} className="w-8" paginator rows={5}
                           rowsPerPageOptions={[5, 10, 25, 50]}>
                    <Column field="surveyId" header="Code" body={surveyCodeTemplate}></Column>
                    <Column field="description" header="Name"></Column>
                    <Column field="completions.length" header="# Completions"></Column>
                    <Column field="closed" header="Closed" body={surveyClosedTemplate}></Column>
                    <Column header="Results" body={surveyResultsTemplate}></Column>
                </DataTable>
            </div>
        </>
    )
}

export default Surveys;
