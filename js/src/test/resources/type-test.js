function test(obj) {
    print('Function "test" is called in js.');
    return {
        jsType: typeof(obj),
        jType: obj.getClass().getName(),
    };
}
