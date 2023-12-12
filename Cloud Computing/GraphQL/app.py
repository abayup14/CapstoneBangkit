from api import app, db
from ariadne import load_schema_from_path, make_executable_schema, graphql_sync, snake_case_fallback_resolvers, ObjectType
from flask import request, jsonify
from ariadne.constants import PLAYGROUND_HTML
# from api.queries import list_posts_resolver, get_post_resolver
from api.queries import list_users_resolver
# from api.mutations import create_post_resolver, update_post_resolver, delete_post_resolver
from api.mutations import create_user_resolver, create_company_resolver, create_lowongan_resolver, create_skills_resolver, create_user_has_skills_resolver

query = ObjectType("Query")
mutation = ObjectType("Mutation")
# query.set_field("listPosts", list_posts_resolver)
# query.set_field("getPost", get_post_resolver)
# mutation.set_field("createPost", create_post_resolver)
# mutation.set_field("updatePost", update_post_resolver)
# mutation.set_field("deletePost", delete_post_resolver)
query.set_field("listUsers", list_users_resolver)
mutation.set_field("createUser", create_user_resolver)

type_defs = load_schema_from_path("schema.graphql")
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
