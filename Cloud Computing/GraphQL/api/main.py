from api import app, db
from ariadne import load_schema_from_path, make_executable_schema, graphql_sync, snake_case_fallback_resolvers, ObjectType
from flask import request, jsonify
#from ariadne.constants import PLAYGROUND_HTML
from api.queries import list_users_resolver, list_companies_resolver, list_lowongans_resolver, list_skills_resolver, \
    list_user_has_skills_resolver, cek_login_user, cek_login_company, list_edukasi_user_resolver, list_pengalaman_user_resolver, \
    list_user_has_skills_resolver,list_lowongans_company_resolver,list_lowongans_user_apply_resolver,list_lowongans_user_search_resolver, \
    list_notifikasi_resolver, list_apply_resolver
from api.mutations import create_user_resolver, create_company_resolver, create_lowongan_resolver, create_skills_resolver, \
    create_user_has_skills_resolver, create_pengalaman_resolver, create_edukasi_resolver, create_skills_required_resolver, \
    create_notifikasi_resolver, create_apply_resolver


query = ObjectType("Query")
mutation = ObjectType("Mutation")


# Untuk query
query.set_field("listUsers", list_users_resolver)
query.set_field("cekLoginUser", cek_login_user)
query.set_field("cekLoginCompany", cek_login_company)
query.set_field("listEdukasiUser", list_edukasi_user_resolver)
query.set_field("listPengalamanUser", list_pengalaman_user_resolver)
query.set_field("listUserSkills", list_user_has_skills_resolver)
query.set_field("listLowonganCompany", list_lowongans_company_resolver)
query.set_field("listLowonganUserSearch", list_lowongans_user_search_resolver)
query.set_field("listLowonganUserApply", list_lowongans_user_apply_resolver)
query.set_field("listNotifikasiUser", list_notifikasi_resolver)
query.set_field("listApply", list_apply_resolver)


#Untuk mutation
mutation.set_field("createUser", create_user_resolver)
mutation.set_field("createCompany", create_company_resolver)
mutation.set_field("createSkills", create_skills_resolver)
mutation.set_field("createPengalaman", create_pengalaman_resolver)
mutation.set_field("createEdukasi", create_edukasi_resolver)
mutation.set_field("createUserHasSkills", create_user_has_skills_resolver)
mutation.set_field("createLowongan", create_lowongan_resolver)
mutation.set_field("createSkillRequired", create_skills_required_resolver)
mutation.set_field("createNotification", create_notifikasi_resolver)
mutation.set_field("createApply", create_apply_resolver)


type_defs = load_schema_from_path("api/schema.graphql")
schema = make_executable_schema(type_defs, query, mutation, snake_case_fallback_resolvers)


@app.route("/graphql", methods=["GET"])
def graphql_server():
    data = request.get_json()
    success, result = graphql_sync(
        schema,
        data,
        context_value=request,
        debug=app.debug
    )
    status_code = 200 if success else 400
    return jsonify(result), status_code
    #return PLAYGROUND_HTML, 200
# def graphql_playground():
#     # return PLAYGROUND_HTML, 200
    


@app.route("/graphql", methods=["POST"])
def graphql_server_post():
    data = request.get_json()
    success, result = graphql_sync(
        schema,
        data,
        context_value=request,
        debug=app.debug
    )
    status_code = 200 if success else 400
    return jsonify(result), status_code
    
if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000)
