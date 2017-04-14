# Scopes in Python

# module level scope
a = 1
def module_function():
    # enclosing function scope
    b = 2
    def inner_function1():
        # innermost scope
        c = 3
        a = 4  # we're creating a new variable
        print a
    inner_function1()
    def inner_function2():
        global a
        a = 5
    inner_function2()


module_function()
print a
