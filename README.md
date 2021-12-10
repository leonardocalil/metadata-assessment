# metadata-assessment
##Pre-requirements:
- Docker version 18.X+
- Docker Compose version v2.X+
- Maven version 3.3.X+
- Java Version: 8

##Building and executing:
- mvn clean package
- docker compose up --build

##Features x API:

###Create Report API for admin user to view all relationships between students and courses
#### students: - GET:/student/all
#### courses: - GET:/course/all

###Create students CRUD 
- GET:/student/get/{studentId}
- POST:/student/create
- PUT:/student/update
- DELETE:/student/delete/{studentId}

###Create courses CRUD
- GET:/course/get/{courseId}
- POST:/course/create
- PUT:/course/update
- DELETE:/course/delete/{courseId}
- DELETE:/course/delete/{courseId}

###Create API for students to register to courses
- GET: /school/assign-student-course/{studentId}/{courseId}

###Filter all students with a specific course
- GET: /school/student-by-course/{courseId}

###Filter all courses for a specific student
- GET: /school/course-by-student/{studentId}

###Filter all courses without any students
- GET: /school/course-no-student

###Filter all students without any courses
- GET: /school/student-no-course

##Testing:
- You can test the APIs using swagger-ui through the url: http://localhost:8080/swagger-ui.html
