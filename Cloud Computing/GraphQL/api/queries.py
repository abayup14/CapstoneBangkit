from .models import User, Company, Lowongan, Skills, UserHasSkills
from ariadne import convert_kwargs_to_snake_case


@convert_kwargs_to_snake_case
def list_users_resolver(obj, info):
    try:
        users = [user.to_dict() for user in User.query.all()]
        payload = {
            "success": True,
            "users": users
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

'''
@convert_kwargs_to_snake_case
def list_companies_resolver(obj, info):
    try:
        companies = [company.to_dict() for company in Company.query.all()]
        payload = {
            "success": True,
            "companies": companies
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_lowongans_resolver(obj, info):
    try:
        lowongans = [lowongan.to_dict() for lowongan in Lowongan.query.all()]
        payload = {
            "success": True,
            "lowongans": lowongans
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_skills_resolver(obj, info):
    try:
        skills = [skill.to_dict() for skill in Skills.query.all()]
        payload = {
            "success": True,
            "skills": skills
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_user_has_skills_resolver(obj, info):
    try:
        user_has_skills = [uhs.to_dict() for uhs in UserHasSkills.query.all()]
        payload = {
            "success": True,
            "user_has_skills": user_has_skills
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload
'''

# @convert_kwargs_to_snake_case
# def list_posts_resolver(obj, info):
#     try:
#         posts = [post.to_dict() for post in Post.query.filter(Post.description.like("%%")).all()]
#         print(posts)
#         payload = {
#             "success": True,
#             "posts": posts
#         }
#     except Exception as e:
#         payload = {
#             "success": False,
#             "errors": [str(e)]
#         }
#     return payload

# @convert_kwargs_to_snake_case
# def list_users_resolver(obj, info):
#     try:
#         users = [user.to_dict() for user in User.query.all()]
#         print(users)
#         payload = {
#             users
#         }
#     except Exception as e:
#         payload = {
#             "success": False,
#             "errors": [str(e)]
#         }
#     return payload


# @convert_kwargs_to_snake_case
# def get_post_resolver(obj, info, id):
#     try:
#         post = Post.query.get(id)
#         payload = {   
#             "success": True,
#             "post": post.to_dict()
#         }
#     except AttributeError:
#         payload = {
#             "success": False,
#             "errors": [f"Post item matching {id} not found"]
#         }
#     return payload

@convert_kwargs_to_snake_case
def list_companies_resolver(obj, info):
    try:
        companies = [company.to_dict() for company in Company.query.all()]
        payload = {
            "success": True,
            "companies": companies
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_lowongans_resolver(obj, info):
    try:
        lowongans = [lowongan.to_dict() for lowongan in Lowongan.query.all()]
        payload = {
            "success": True,
            "lowongans": lowongans
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_skills_resolver(obj, info):
    try:
        skills = [skill.to_dict() for skill in Skills.query.all()]
        payload = {
            "success": True,
            "skills": skills
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload

@convert_kwargs_to_snake_case
def list_user_has_skills_resolver(obj, info):
    try:
        user_has_skills = [uhs.to_dict() for uhs in UserHasSkills.query.all()]
        payload = {
            "success": True,
            "user_has_skills": user_has_skills
        }
    except Exception as e:
        payload = {
            "success": False,
            "errors": [str(e)]
        }
    return payload
